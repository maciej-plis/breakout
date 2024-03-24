package com.matthias.breakout.screen

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.matthias.breakout.BreakoutGame
import com.matthias.breakout.assets.AtlasAsset.BREAKOUT_ATLAS
import com.matthias.breakout.assets.TiledMapAsset.LEVEL_1
import com.matthias.breakout.common.get
import com.matthias.breakout.common.toMeters
import com.matthias.breakout.contact.*
import com.matthias.breakout.ecs.component.*
import com.matthias.breakout.ecs.system.*
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.plusAssign
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.circle
import ktx.log.logger
import ktx.tiled.*
import kotlin.experimental.inv
import kotlin.random.Random

private val LOG = logger<GameScreen>()

class GameScreen(game: BreakoutGame) : StageScreenBase(game) {

    private val eventManager by lazy {
        GameEventManager<GameEvent>()
    }

    private val world by lazy {
        World(Vector2(0f, 0f), true).apply {
            setContactListener(GameContactListener(eventManager))
        }
    }

    private val engine by lazy {
        PooledEngine().apply {
            addSystem(PhysicsSystem(world))
            addSystem(PaddleKeyboardMovementSystem())
            addSystem(PaddleMouseMovementSystem(viewport))
            addSystem(PaddleLiftSystem())
            addSystem(PaddleBoundarySystem(0f, camera.viewportWidth))
            addSystem(AttachSystem())
            addSystem(PaddleStickingSystem(eventManager))
            addSystem(BallReleaseSystem())
            addSystem(PaddleBounceSystem(eventManager))
            addSystem(WallBounceSystem(eventManager))
            addSystem(BlockBounceSystem(eventManager))
            addSystem(BallAngleBoundarySystem())
            addSystem(BlockDestroySystem(eventManager))
            addSystem(GameOverSystem(eventManager, 0f))
            addSystem(DebugSystem { createBall(this.getEntitiesFor(allOf(PaddleComponent::class).get()).first()) })
            addSystem(RemoveSystem(world))
            addSystem(PhysicsSyncSystem())
//            addSystem(PhysicsDebugRenderingSystem(world, camera))
            addSystem(RenderSystem(batch, viewport))
        }
    }

    override val clearColor = Color.valueOf("#2B242B")

    override val viewport = game.gameViewport

    override fun show() {
        super.show()

        createTopWall()
        createLeftWall()
        createRightWall()
        val paddle = createPaddle()
        createBall(paddle)
        createBlocks()
    }

    override fun update(delta: Float) {
        engine.update(delta)
        eventManager.clear()
    }

    private fun createTopWall() {
        engine.entity {
            with<WallComponent>()
            with<TransformComponent> {
                this.setPosition(camera.viewportWidth / 2, camera.viewportHeight + 0.5f.toMeters())
                size.set(camera.viewportWidth, 1f.toMeters())
            }
            entity += BodyComponent(
                world.body {
                    position.set(camera.viewportWidth / 2, camera.viewportHeight + 0.5f.toMeters())
                    box(camera.viewportWidth, 1f.toMeters()) {
                        filter.categoryBits = WALL_BIT
                    }
                    userData = entity
                }
            )
        }
    }

    private fun createLeftWall() {
        engine.entity {
            with<WallComponent>()
            with<TransformComponent> {
                this.setPosition((-0.5f).toMeters(), camera.viewportHeight / 2)
                size.set(1f.toMeters(), camera.viewportHeight)
            }
            entity += BodyComponent(
                world.body {
                    position.set((-0.5f).toMeters(), camera.viewportHeight / 2)
                    box(1f.toMeters(), camera.viewportHeight) {
                        filter.categoryBits = WALL_BIT
                    }
                    userData = entity
                }
            )
        }
    }

    private fun createRightWall() {
        engine.entity {
            with<WallComponent>()
            with<TransformComponent> {
                this.setPosition(camera.viewportWidth + 0.5f.toMeters(), camera.viewportHeight / 2)
                size.set(1f.toMeters(), camera.viewportHeight)
            }
            entity += BodyComponent(
                world.body {
                    position.set(camera.viewportWidth + 0.5f.toMeters(), camera.viewportHeight / 2)
                    box(1f.toMeters(), camera.viewportHeight) {
                        filter.categoryBits = WALL_BIT
                    }
                    userData = entity
                }
            )
        }
    }

    private fun createPaddle(): Entity {
        val atlas = assets[BREAKOUT_ATLAS.descriptor]
        val texture = atlas["paddle-m"]
        return engine.entity {
            with<PaddleComponent>()
            with<GraphicComponent> {
                setSpriteRegion(texture)
            }
            with<TransformComponent> {
                this.setPosition(camera.viewportWidth / 2, (-5f).toMeters())
                size.set(7f.toMeters(), 1.5f.toMeters())
            }
            entity += BodyComponent(
                world.body {
                    position.set(camera.viewportWidth / 2, (-5f).toMeters())
                    box(width = 7f.toMeters(), height = 1.5f.toMeters()) {
                        filter.categoryBits = PADDLE_BIT
                    }
                    userData = entity
                }
            )
        }
    }

    private fun createBall(paddle: Entity) {
        val atlas = assets[BREAKOUT_ATLAS.descriptor]
        val texture = atlas["ball"]
        engine.entity {
            with<BallComponent>() {
                velocity = 32f.toMeters()
            }
            with<TransformComponent> {
                this.setPosition(camera.viewportWidth / 2, 10f.toMeters())
                size.set(1.25f, 1.25f).toMeters()
            }
            with<GraphicComponent> {
                setSpriteRegion(texture)
            }
            with<AttachComponent>().apply {
                targetEntity = paddle
                val xOffset = Random.nextFloat() / 2 - 0.25f
                offset = Vector2(xOffset.toMeters(), (1.25f + 1.5f).toMeters() / 2)
            }
            entity += BodyComponent(
                world.body(type = DynamicBody) {
                    position.set(camera.viewportWidth / 2, 10f.toMeters())
                    circle((1.25f / 2).toMeters()) {
                        filter.categoryBits = BALL_BIT
                        filter.maskBits = BALL_BIT.inv()
                        restitution = 1f
                    }
                    userData = entity
                    bullet = true
                    fixedRotation = true
                }
            )
        }
    }

    private fun createBlocks() {
        val atlas = assets[BREAKOUT_ATLAS.descriptor]
        val level1 = assets[LEVEL_1.descriptor]

        level1.forEachMapObject("blocks") {
            val texture = atlas[it.property("texture")]
            val width = it.width.toMeters().toMeters()
            val height = it.height.toMeters().toMeters()
            val x = it.x.toMeters().toMeters() + width / 2
            val y = it.y.toMeters().toMeters() + height / 2
            engine.entity {
                with<BlockComponent>()
                with<GraphicComponent>().apply { setSpriteRegion(texture) }
                with<TransformComponent> {
                    this.setPosition(x, y)
                    size.set(width, height)
                }
                entity += BodyComponent(
                    world.body {
                        position.set(x, y)
                        box(width, height) {
                            filter.categoryBits = BLOCK_BIT
                        }
                        userData = entity
                    }
                )
            }
        }
    }
}