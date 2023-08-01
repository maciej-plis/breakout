package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.matthias.breakout.common.reflect
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.BallComponent
import com.matthias.breakout.ecs.component.BodyComponent
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallWallHit
import com.matthias.breakout.event.GameEventManager
import ktx.ashley.get
import ktx.log.logger

private val LOG = logger<WallBounceSystem>()

class WallBounceSystem(private val eventManager: GameEventManager<GameEvent>) : EntitySystem() {

    override fun update(delta: Float) {
        eventManager.forEachEventOfType<BallWallHit> { event ->
            val ballC = event.ballEntity[BallComponent.mapper]!!
            val bodyC = event.ballEntity[BodyComponent.mapper]!!

            val reflectedAngle = event.ballContactVelocity.reflect(event.contactNormal).angleRad()

            LOG.debug { "Ball hit wall, reflected on angle ${reflectedAngle * radiansToDegrees}" }
            bodyC.body.let { ball ->
                ball.linearVelocity = velocityOnAngle(ballC.velocity, reflectedAngle)
                ball.setTransform(ball.position, reflectedAngle)
            }
        }
    }
}