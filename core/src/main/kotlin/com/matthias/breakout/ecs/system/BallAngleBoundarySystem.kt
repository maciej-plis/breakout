package com.matthias.breakout.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.matthias.breakout.common.compareTo
import com.matthias.breakout.common.missingComponent
import com.matthias.breakout.common.toRadians
import com.matthias.breakout.common.velocityOnAngle
import com.matthias.breakout.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.log.logger
import kotlin.math.abs
import kotlin.math.sign

private val LOG = logger<BallAngleBoundarySystem>()
private val FAMILY = allOf(BallComponent::class, BodyComponent::class).exclude(RemoveComponent::class, AttachComponent::class).get()

private val BALL_ALLOWED_DEGREES_RANGE = 30f..150f

/**
 * System responsible for keeping ball angle in specified range.
 *
 * --
 *
 * **Family**:
 * - allOf: [[BallComponent], [BodyComponent]]
 * - exclude: [[RemoveComponent], [AttachComponent]]
 */
class BallAngleBoundarySystem : IteratingSystem(FAMILY) {

    override fun processEntity(entity: Entity, delta: Float) {
        val ballC = entity[BallComponent::class] ?: return LOG.missingComponent<BallComponent>()
        val bodyC = entity[BodyComponent::class] ?: return LOG.missingComponent<BodyComponent>()

        val angle = abs(bodyC.angleDeg) % 180
        val angleSign = bodyC.angleDeg.sign
        val correctedAngle = when {
            angle < BALL_ALLOWED_DEGREES_RANGE -> BALL_ALLOWED_DEGREES_RANGE.start * angleSign
            angle > BALL_ALLOWED_DEGREES_RANGE -> BALL_ALLOWED_DEGREES_RANGE.endInclusive * angleSign
            else -> return
        }

        LOG.debug { "Ball incorrect angle ${bodyC.angleDeg} corrected to $correctedAngle" }
        bodyC.setAngle(correctedAngle.toRadians())
        bodyC.setVelocity(velocityOnAngle(ballC.velocity, correctedAngle.toRadians()))
    }
}
