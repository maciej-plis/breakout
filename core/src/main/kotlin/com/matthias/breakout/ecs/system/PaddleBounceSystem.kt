package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils.degreesToRadians
import com.matthias.breakout.common.halfWidth
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.ecs.component.TransformComponent
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallPaddleHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.get

class PaddleBounceSystem(private val eventManager: GameEventManager<GameEvent>) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.forEventsOfType<BallPaddleHit> { event ->
            val ballBodyC = event.ballEntity[BodyComponent.mapper]!!
            val ballC = event.ballEntity[BallComponent.mapper]!!

            val paddleTransformC = event.paddleEntity[TransformComponent.mapper]!!

            val ball = ballBodyC.body
            val paddleContactPoint = event.paddleContactPoint

            val percent = paddleContactPoint.x / paddleTransformC.size.halfWidth
            val angle = ((-60 * percent) + 90)

            ball.linearVelocity = velocityOnAngle(ballC.velocity, angle * degreesToRadians)
            ball.setTransform(ball.position, ball.linearVelocity.angleRad())
        }
    }
}