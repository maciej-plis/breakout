package com.matthias.breakout.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils.degreesToRadians
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.GdxRuntimeException
import com.matthias.breakout.BreakoutGame
import com.matthias.breakout.assets.AtlasAsset.BREAKOUT_ATLAS
import com.matthias.breakout.common.half
import com.matthias.breakout.common.toMeters
import com.matthias.breakout.contact.*
import com.matthias.breakout.ecs.component.*
import com.matthias.breakout.ecs.system.*
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.entity
import ktx.ashley.plusAssign
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.circle
import ktx.log.logger
import kotlin.experimental.inv

private val LOG = logger<GameScreen>()

class GameScreen(game: BreakoutGame) : ScreenBase(game) {

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
            addSystem(PaddleMouseMovementSystem(camera))
            addSystem(PaddleBoundarySystem(0f.toMeters(), camera.viewportWidth))
            addSystem(PaddleBounceSystem(eventManager))
            addSystem(WallBounceSystem(eventManager))
            addSystem(BlockBounceSystem(eventManager))
            addSystem(BallAngleBoundarySystem())
            addSystem(BlockDestroySystem(eventManager))
            addSystem(GameOverSystem(eventManager, 0f))
            addSystem(DebugSystem { createBall() })
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
        createPaddle()
        createBall()
        createBlocks()
    }

    override fun update(delta: Float) {
        engine.update(delta)
        eventManager.clear()
    }

    private fun createTopWall() {
        engine.entity {
            with<TransformComponent> {
                setInitialPosition(camera.viewportWidth / 2, camera.viewportHeight + 0.5f.toMeters(), 1f)
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
            with<TransformComponent> {
                setInitialPosition((-0.5f).toMeters(), camera.viewportHeight / 2, 1f)
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
            with<TransformComponent> {
                setInitialPosition(camera.viewportWidth + 0.5f.toMeters(), camera.viewportHeight / 2, 1f)
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

    private fun createPaddle() {
        val atlas = assets[BREAKOUT_ATLAS.descriptor]
        val texture = atlas["paddle-m"]
        engine.entity {
            with<PaddleComponent> {
                speed = 0.5f.toMeters()
            }
            with<GraphicComponent> {
                setSpriteRegion(texture)
            }
            with<TransformComponent> {
                setInitialPosition(camera.viewportWidth / 2, 1.5f.toMeters(), 1f)
                size.set(7f.toMeters(), 1.5f.toMeters())
            }
            entity += BodyComponent(
                world.body {
                    position.set(camera.viewportWidth / 2, 1.5f.toMeters())
                    box(width = 7f.toMeters(), height = 1.5f.toMeters()) {
                        filter.categoryBits = PADDLE_BIT
                    }
                    userData = entity
                }
            )
        }
    }

    private fun createBall() {
        val atlas = assets[BREAKOUT_ATLAS.descriptor]
        val texture = atlas["ball"]
        engine.entity {
            with<BallComponent>() {
                velocity = 32f.toMeters()
            }
            with<TransformComponent> {
                setInitialPosition(camera.viewportWidth / 2, camera.viewportHeight / 2, 1f)
                size.set(1.25f, 1.25f).toMeters()
            }
            with<GraphicComponent> {
                setSpriteRegion(texture)
            }
            entity += BodyComponent(
                world.body(type = DynamicBody) {
                    position.set(camera.viewportWidth / 2, camera.viewportHeight / 2)
                    circle((1.25f / 2).toMeters()) {
                        filter.categoryBits = BALL_BIT
                        filter.maskBits = BALL_BIT.inv()
                        restitution = 1f
                    }
                    userData = entity
                    bullet = true
                    fixedRotation = true
                    angle = -90f * degreesToRadians
                    linearVelocity.set(0f, -16f).toMeters()
                }
            )
        }
    }

    private fun createBlocks() {
        val blocksPerLine = 10
        val blockRows = 7
        val spacing = 0.5f.toMeters()
        val lineSpace = (camera.viewportWidth - 2f.toMeters()) - (spacing * (blocksPerLine + 1))

//        val blockWidth = lineSpace / blocksPerLine
        val blockWidth = 5f.toMeters()
        val blockHeight = 2f.toMeters()

        val atlas = assets[BREAKOUT_ATLAS.descriptor]

        val blockPos = Vector2(1f.toMeters() + (spacing + blockWidth.half), camera.viewportHeight - 1f.toMeters() + (-spacing - blockHeight.half))
        for (i in 1..blockRows) {
            val texture = atlas["block-$i"]
            for (j in 1..blocksPerLine - 2) {
                engine.entity {
                    with<BlockComponent>()
                    with<GraphicComponent>().apply { setSpriteRegion(texture) }
                    with<TransformComponent> {
                        setInitialPosition(blockPos.x, blockPos.y, 1f)
                        size.set(blockWidth, blockHeight)
                    }
                    entity += BodyComponent(
                        world.body {
                            position.set(blockPos.x, blockPos.y)
                            box(blockWidth, blockHeight) {
                                filter.categoryBits = BLOCK_BIT
                            }
                            userData = entity
                        }
                    )
                }
                blockPos.set(blockPos.x + blockWidth + spacing, blockPos.y)
            }
            blockPos.set(1f.toMeters() + (spacing + blockWidth.half), blockPos.y - blockHeight - spacing)
        }
    }
}

private operator fun TextureAtlas.get(name: String) = findRegion(name) ?: throw GdxRuntimeException("Region '$name' not found in atlas")
