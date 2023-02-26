package com.matthias.breakout.contact

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.BallPaddleHit
import com.matthias.breakout.event.GameEvent.BallWallHit
import com.matthias.breakout.event.GameEventManager
import kotlin.experimental.or

const val BALL_BIT: Short = 1
const val PADDLE_BIT: Short = 2
const val WALL_BIT: Short = 4

class GameContactListener(private val eventManager: GameEventManager<GameEvent>) : ContactListener {

    override fun beginContact(contact: Contact) {
        when (contact.fixtureA.filterData.categoryBits or contact.fixtureB.filterData.categoryBits) {
            BALL_BIT or PADDLE_BIT -> eventManager.addEvent(
                BallPaddleHit.apply {
                    paddleContactPoint.set(getBody(contact, PADDLE_BIT).getLocalPoint(contact.worldManifold.points.first()))
                }
            )

            BALL_BIT or WALL_BIT -> eventManager.addEvent(
                BallWallHit.apply {
                    contactNormal.set(contact.worldManifold.normal)
                    ballContactVelocity.set(getBody(contact, BALL_BIT).linearVelocity)
                }
            )
        }
    }

    override fun endContact(contact: Contact) = Unit

    override fun preSolve(contact: Contact, oldManifold: Manifold?) = Unit

    override fun postSolve(contact: Contact, impulse: ContactImpulse?) = Unit

    private fun getBody(contact: Contact, categoryBit: Short) = when {
        contact.fixtureA.filterData.categoryBits == categoryBit -> contact.fixtureA.body
        contact.fixtureB.filterData.categoryBits == categoryBit -> contact.fixtureB.body
        else -> throw IllegalStateException()
    }
}