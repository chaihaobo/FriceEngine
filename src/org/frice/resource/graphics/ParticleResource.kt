package org.frice.resource.graphics

import org.frice.Game
import org.frice.platform.adapter.JvmImage
import org.frice.resource.FResource
import org.frice.resource.graphics.ColorResource.Companion.BLACK
import org.frice.resource.graphics.ColorResource.Companion.COLORLESS
import org.frice.resource.image.ImageResource
import java.awt.Image
import java.util.*

/**
 * Particle effects
 * Created by ice1000 on 2016/8/17.
 *
 * @author ice1000
 * @since v0.3.2
 */
class ParticleResource
@JvmOverloads
constructor(
		val game: Game,
		var width: Int,
		var height: Int,
		val back: FResource = COLORLESS,
		var fore: ColorResource = BLACK,
		var percentage: Double = 0.5) : FResource {
	constructor(game: Game, width: Int, height: Int, percentage: Double) :
			this(game, width, height, COLORLESS, BLACK, percentage)

	/**
	 * particle effects as an image
	 */
	private val image = JvmImage(width, height)
	private val random = Random(Random().nextLong())

	private fun drawBackground() {
		val g = image.image.graphics
		when (back) {
			is ColorResource -> {
				g.color = back.color
				g.fillRect(0, 0, width, height)
			}
			is ImageResource -> g.drawImage((back.image as Image), 0, 0, width, height, game)
		}
	}

	init {
		drawBackground()
		repeat((image.width * image.height * percentage).toInt()) {
			image[random.nextInt(width), random.nextInt(height)] = fore
		}
	}

	override fun getResource() = image.apply {
		drawBackground()
		var cache1: Int
		var cache2: Int
		repeat((image.width * image.height * percentage).toInt()) {
			cache1 = random.nextInt(width)
			cache2 = random.nextInt(height)
			image.setRGB(random.nextInt(width), random.nextInt(height), fore.color.rgb)
			image.setRGB(cache1, cache2, when (back) {
				is ColorResource -> back.color.rgb
				is ImageResource -> back.image[cache1, cache2].color.rgb
				else -> COLORLESS.color.rgb
			})
		}
	}
}