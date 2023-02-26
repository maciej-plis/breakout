package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.degreesToRadians
import com.matthias.breakout.common.halfWidth
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.PaddleComponent
import com.matthias.breakout.ecs.component.TransformComponent
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallPaddleHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.allOf
import ktx.ashley.get

class PaddleBounceSystem(
    private val eventManager: GameEventManager<GameEvent>
) : IteratingSystem(allOf(BallComponent::class, TransformComponent::class, BodyComponent::class).get()) {

    private val paddleEntity: Entity
        get() = engine.getEntitiesFor(allOf(PaddleComponent::class, TransformComponent::class).get()).first()

    override fun processEntity(ballEntity: Entity, delta: Float) {
        eventManager.forEvent<BallPaddleHit> { event ->
            val ballBodyC = ballEntity[BodyComponent.mapper]!!
            val ballC = ballEntity[BallComponent.mapper]!!

            val paddleTransformC = paddleEntity[TransformComponent.mapper]!!

            val ball = ballBodyC.body
            val paddleContactPoint = event.paddleContactPoint

            val percent = paddleContactPoint.x / paddleTransformC.size.halfWidth
            val angle = ((-60 * percent) + 90)

            ball.linearVelocity = velocityOnAngle(ballC.velocity, angle * degreesToRadians)
            ball.setTransform(ball.position, ball.linearVelocity.angleRad())
        }
    }
}