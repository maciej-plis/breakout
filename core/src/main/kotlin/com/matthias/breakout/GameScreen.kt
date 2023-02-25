package com.matthias.breakout

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.LEFT
import com.badlogic.gdx.Input.Keys.RIGHT
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.matthias.breakout.common.toMeters
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.PaddleComponent
import com.matthias.breakout.ecs.component.TransformComponent
import com.matthias.breakout.ecs.system.PaddleBoundarySystem
import com.matthias.breakout.ecs.system.PhysicsDebugRenderingSystem
import com.matthias.breakout.ecs.system.PhysicsSystem
import ktx.app.clearScreen
import ktx.ashley.entity
import ktx.ashley.plusAssign
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.circle
import ktx.log.logger

private val LOG = logger<GameScreen>()

class GameScreen(game: BreakoutGame) : ScreenBase(game) {

    val world by lazy {
        World(Vector2(0f, 0f), true).apply {
            setContactListener(TestContactListener())
        }
    }

    val engine by lazy {
        PooledEngine().apply {
            addSystem(PhysicsSystem(world))
            addSystem(PaddleBoundarySystem(1f.toMeters(), camera.viewportWidth - 1f.toMeters()))
            addSystem(PhysicsDebugRenderingSystem(world, camera))
        }
    }

    private lateinit var ball: Body
    private lateinit var paddle: Body

    override fun show() {
        LOG.info { "Showing ${javaClass.simpleName}" }

        createCeiling()
        createLeftWall()
        createRightWall()
        createPaddle()
        createBall()

        camera.position.set((camera.viewportWidth / 2f), (camera.viewportHeight / 2f), 0f)
        Gdx.input.inputProcessor = InputMultiplexer(TestInputProcessor(paddle, camera))
    }

    override fun render(delta: Float) {
        clearScreen(.40784314f, .40784314f, .5294118f)

        game.gameViewport.apply()
        engine.update(delta)

        if (ball.position.y < paddle.position.y - .5f.toMeters()) {
            ball.setTransform(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f)
            ball.setLinearVelocity(0f, (-16f).toMeters())
        }

        if (paddle.userData == LEFT) {
            paddle.setTransform(paddle.position.x - 0.5f.toMeters(), paddle.position.y, 0f)
        } else if (paddle.userData == RIGHT) {
            paddle.setTransform(paddle.position.x + 0.5f.toMeters(), paddle.position.y, 0f)
        }
    }

    override fun resize(width: Int, height: Int) {
        game.gameViewport.update(width, height)
    }

    override fun dispose() {
        LOG.info { "Disposing ${javaClass.simpleName}" }
        world.dispose()
    }

    private fun createCeiling() {
        engine.entity {
            with<TransformComponent> {
                setInitialPosition(camera.viewportWidth / 2, camera.viewportHeight - 0.5f.toMeters(), 1f)
                size.set(camera.viewportWidth, 1f.toMeters())
            }
            entity += BodyComponent(
                world.body {
                    position.set(camera.viewportWidth / 2, camera.viewportHeight - 0.5f.toMeters())
                    box(camera.viewportWidth, 1f.toMeters()) {
                        filter.categoryBits = 4
                    }
                }
            )
        }
    }

    private fun createLeftWall() {
        engine.entity {
            with<TransformComponent> {
                setInitialPosition(0.5f.toMeters(), camera.viewportHeight / 2, 1f)
                size.set(1f.toMeters(), camera.viewportHeight)
            }
            entity += BodyComponent(
                world.body {
                    position.set(0.5f.toMeters(), camera.viewportHeight / 2)
                    box(1f.toMeters(), camera.viewportHeight) {
                        filter.categoryBits = 8
                    }
                }
            )
        }
    }

    private fun createRightWall() {
        engine.entity {
            with<TransformComponent> {
                setInitialPosition(camera.viewportWidth - 0.5f.toMeters(), camera.viewportHeight / 2, 1f)
                size.set(1f.toMeters(), camera.viewportHeight)
            }
            entity += BodyComponent(
                world.body {
                    position.set(camera.viewportWidth - 0.5f.toMeters(), camera.viewportHeight / 2)
                    box(1f.toMeters(), camera.viewportHeight) {
                        filter.categoryBits = 8
                    }
                }
            )
        }
    }

    private fun createPaddle() {
        engine.entity {
            with<PaddleComponent>()
            with<TransformComponent> {
                setInitialPosition(camera.viewportWidth / 2, 1.5f.toMeters(), 1f)
                size.set(10f.toMeters(), 1f)
            }
            entity += BodyComponent(
                world.body {
                    position.set(camera.viewportWidth / 2, 1.5f.toMeters())
                    box(width = 10f.toMeters(), height = 1f.toMeters()) {
                        filter.categoryBits = 2
                    }
                }.also { paddle = it }
            )
        }
    }

    private fun createBall() {
        engine.entity {
            with<TransformComponent> {
                setInitialPosition(camera.viewportWidth / 2, camera.viewportHeight / 2, 1f)
                size.set(1f, 1f)
            }
            entity += BodyComponent(
                world.body(type = DynamicBody) {
                    position.set(camera.viewportWidth / 2, camera.viewportHeight / 2)
                    circle(0.5f / PPM) {
                        filter.categoryBits = 1
                        userData = -90f * degreesToRadians
                    }
                    fixedRotation = true
                    linearVelocity.set(0f / PPM, -16f / PPM)
                }.also { ball = it }
            )
        }
    }
}

class TestInputProcessor(val paddle: Body, val camera: Camera) : InputProcessor {
    override fun keyDown(keycode: Int): Boolean {
        paddle.userData = keycode
        return true;
    }

    override fun keyUp(keycode: Int): Boolean {
        if (paddle.userData == keycode) paddle.userData = null
        return true;
    }

    override fun keyTyped(character: Char) = TODO()

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = TODO()

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = TODO()

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = TODO()

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val coords = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
        paddle.setTransform(coords.x, paddle.position.y, 0f)
        return true;
    }

    override fun scrolled(amountX: Float, amountY: Float) = TODO()

}

class TestContactListener : ContactListener {

    override fun beginContact(contact: Contact) {
        if (contact.fixtureA.filterData.categoryBits.toInt() == 1 && contact.fixtureB.filterData.categoryBits.toInt() == 2) {
            val cp = contact.worldManifold.points[0]
            val point = contact.fixtureB.body.getLocalPoint(cp)

            val x = (point.x * PPM) / 5

            val angle = ((60 * x) + 90) * degreesToRadians
            contact.fixtureA.userData = angle

            contact.fixtureA.body.linearVelocity = velocityOnAngle(32f / PPM, angle)
        } else if (contact.fixtureA.filterData.categoryBits.toInt() == 2 && contact.fixtureB.filterData.categoryBits.toInt() == 1) {
            val cp = contact.worldManifold.points[0]
            val point = contact.fixtureA.body.getLocalPoint(cp)

            val x = (point.x * PPM) / 5

            val angle = ((-60 * x) + 90) * degreesToRadians
            contact.fixtureB.userData = angle

            contact.fixtureB.body.linearVelocity = velocityOnAngle(32f / PPM, angle)
        }

        if (contact.fixtureA.filterData.categoryBits.toInt() == 1 && contact.fixtureB.filterData.categoryBits.toInt() == 8) {
            contact.fixtureA.body.setLinearVelocity(-contact.fixtureA.body.linearVelocity.x, contact.fixtureA.body.linearVelocity.y)
        } else if (contact.fixtureA.filterData.categoryBits.toInt() == 8 && contact.fixtureB.filterData.categoryBits.toInt() == 1) {
            contact.fixtureB.body.setLinearVelocity(-contact.fixtureB.body.linearVelocity.x, contact.fixtureB.body.linearVelocity.y)
        }

        if (contact.fixtureA.filterData.categoryBits.toInt() == 1 && contact.fixtureB.filterData.categoryBits.toInt() == 4) {
            contact.fixtureA.body.setLinearVelocity(contact.fixtureA.body.linearVelocity.x, -contact.fixtureA.body.linearVelocity.y)
        } else if (contact.fixtureA.filterData.categoryBits.toInt() == 4 && contact.fixtureB.filterData.categoryBits.toInt() == 1) {
            contact.fixtureB.body.setLinearVelocity(contact.fixtureB.body.linearVelocity.x, -contact.fixtureB.body.linearVelocity.y)
        }
    }

    override fun endContact(contact: Contact) = Unit

    override fun preSolve(contact: Contact, oldManifold: Manifold?) = Unit

    override fun postSolve(contact: Contact, impulse: ContactImpulse?) = Unit

    private fun velocityOnAngle(velocity: Float, angle: Float) = Vector2(cos(angle) * velocity, sin(angle) * velocity)
}