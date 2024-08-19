package org.demo

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.TimeUtils
import kotlin.properties.Delegates


class Drop : ApplicationAdapter() {

    private lateinit var dropImage: Texture
    private lateinit var bucketImage: Texture
    private lateinit var dropSound: Sound
    private lateinit var rainMusic: Music
    private lateinit var batch: SpriteBatch

    private lateinit var camera: OrthographicCamera

    private lateinit var bucket: Rectangle
    private lateinit var touchPos: Vector3
    private lateinit var raindrops: Array<Rectangle>
    private var lastDropTime by Delegates.notNull<Long>()

    override fun dispose() {
        super.dispose()
        dropImage.dispose()
        bucketImage.dispose()
        dropSound.dispose()
        rainMusic.dispose()
        batch.dispose()
    }

    override fun create(){
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = Texture(Gdx.files.internal("droplet.png"))
        bucketImage = Texture(Gdx.files.internal("bucket.png"))

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3")) // longer than 10 seconds

        // start the playback of the background music immediately
        rainMusic.isLooping = true
        rainMusic.play()

        // create the camera
        camera = OrthographicCamera()
        camera.setToOrtho(false, 800F, 480F)

        // create the sprite batch
        batch = SpriteBatch()

        // create the backet
        bucket = Rectangle()
        bucket.x = (800 / 2 - 64 / 2).toFloat() // center on X
        bucket.y = 20f // fixed y location
        bucket.width = 64f
        bucket.height = 64f

        // initialize touch position variable
        touchPos = Vector3()

        // initialize raindrops array and create a raindrop
        raindrops = Array<Rectangle>()
        lastDropTime = 0L
    }

    override fun render() {
        ScreenUtils.clear(0f, 0f, 0.2f, 1f)

        // update the bucket position - touch/mouse
        if (Gdx.input.isTouched) {
            touchPos[Gdx.input.x.toFloat(), Gdx.input.y.toFloat()] = 0f
            camera.unproject(touchPos)
            bucket.x = touchPos.x - 64 / 2
        }

        // update the bucket position - keyboard
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.deltaTime;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.deltaTime;

        // create bucket coordinate boundaries
        if(bucket.x < 0) bucket.x = 0F;
        if(bucket.x > 800 - 64) bucket.x = (800 - 64).toFloat();

        // create raindrops
        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

        // move raindrops
        raindrops.forEachIndexed { index, raindrop ->
            raindrop.y -= 200 * Gdx.graphics.deltaTime
            if(raindrop.overlaps(bucket)) {
                dropSound.play()
                raindrops.removeIndex(index)
            }
            if (raindrop.y + 64 < 0) raindrops.removeIndex(index)
        }

        // render assets
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        raindrops.forEach{ raindrop -> batch.draw(dropImage, raindrop.x, raindrop.y) }
        batch.end();

        camera.update();
    }

    private fun spawnRaindrop() {
        val raindrop = Rectangle()
        raindrop.x = MathUtils.random(0, 800 - 64).toFloat()
        raindrop.y = 480f
        raindrop.width = 64f
        raindrop.height = 64f
        raindrops.add(raindrop)
        lastDropTime = TimeUtils.nanoTime()
    }

}
