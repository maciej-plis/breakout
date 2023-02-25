package com.matthias.breakout.contact

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.matthias.breakout.PPM

class GameContactListener : ContactListener {

    override fun beginContact(contact: Contact) {
        if (contact.fixtureA.filterData.categoryBits.toInt() == 1 && contact.fixtureB.filterData.categoryBits.toInt() == 2) {
            val cp = contact.worldManifold.points[0]
            val point = contact.fixtureB.body.getLocalPoint(cp)

            val x = (point.x * PPM) / 5

            val angle = ((60 * x) + 90) * MathUtils.degreesToRadians
            contact.fixtureA.userData = angle

            contact.fixtureA.body.linearVelocity = velocityOnAngle(32f / PPM, angle)
        } else if (contact.fixtureA.filterData.categoryBits.toInt() == 2 && contact.fixtureB.filterData.categoryBits.toInt() == 1) {
            val cp = contact.worldManifold.points[0]
            val point = contact.fixtureA.body.getLocalPoint(cp)

            val x = (point.x * PPM) / 5

            val angle = ((-60 * x) + 90) * MathUtils.degreesToRadians
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

    private fun velocityOnAngle(velocity: Float, angle: Float) = Vector2(MathUtils.cos(angle) * velocity, MathUtils.sin(angle) * velocity)
}