package com.matthias.breakout.contact

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.matthias.breakout.event.GameEvent
import com.matthias.breakout.event.GameEvent.*
import com.matthias.breakout.event.GameEventManager
import kotlin.experimental.or

const val BALL_BIT: Short = 1
const val PADDLE_BIT: Short = 2
const val WALL_BIT: Short = 4
const val BLOCK_BIT: Short = 8

class GameContactListener(private val eventManager: GameEventManager<GameEvent>) : ContactListener {

    override fun beginContact(contact: Contact) {
        when (contact.fixtureA.filterData.categoryBits or contact.fixtureB.filterData.categoryBits) {
            BALL_BIT or PADDLE_BIT -> eventManager.addEvent(
                BallPaddleHit(
                    getEntity(contact, BALL_BIT),
                    getEntity(contact, PADDLE_BIT),
                    Vector2(getBody(contact, PADDLE_BIT).getLocalPoint(contact.worldManifold.points.first()))
                )
            )

            BALL_BIT or WALL_BIT -> eventManager.addEvent(
                BallWallHit(
                    getEntity(contact, BALL_BIT),
                    getEntity(contact, WALL_BIT),
                    Vector2(contact.worldManifold.normal),
                    Vector2(getBody(contact, BALL_BIT).linearVelocity)
                )
            )

            BALL_BIT or BLOCK_BIT -> eventManager.addEvent(
                BallBlockHit(
                    getEntity(contact, BALL_BIT),
                    getEntity(contact, BLOCK_BIT),
                    Vector2(contact.worldManifold.normal),
                    Vector2(getBody(contact, BALL_BIT).linearVelocity)
                )
            )
        }
    }

    override fun endContact(contact: Contact) = Unit

    override fun preSolve(contact: Contact, oldManifold: Manifold) = Unit

    override fun postSolve(contact: Contact, impulse: ContactImpulse) = Unit

    private fun getFixture(contact: Contact, categoryBit: Short) = when {
        contact.fixtureA.filterData.categoryBits == categoryBit -> contact.fixtureA
        contact.fixtureB.filterData.categoryBits == categoryBit -> contact.fixtureB
        else -> throw IllegalStateException("No participant in contact for categoryBit")
    }

    private fun getBody(contact: Contact, categoryBit: Short) = getFixture(contact, categoryBit).body

    private fun getEntity(contact: Contact, categoryBit: Short) = getEntity(getBody(contact, categoryBit))

    private fun getEntity(body: Body) = (body.userData as? Entity) ?: throw IllegalStateException("Body has no entity assigned")
}