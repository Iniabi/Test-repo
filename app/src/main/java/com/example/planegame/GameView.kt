package com.example.planegame

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), Runnable {
    private val thread: Thread
    private var isPlaying = false
    private var canvas: Canvas? = null
    private var paint: Paint
    private var background: Bitmap
    private var player: Bitmap
    private var enemy: Bitmap
    
    // Player position
    private var playerX = 0f
    private var playerY = 0f
    
    // Game elements
    private val enemies = ArrayList<RectF>()
    private val bullets = ArrayList<RectF>()
    private var score = 0
    
    init {
        // Initialize game objects
        background = Bitmap.createBitmap(800, 1200, Bitmap.Config.ARGB_8888)
        Canvas(background).apply {
            drawColor(Color.BLACK)
            for (i in 0..30) {
                paint.color = Color.argb(255, 255, 255, 255)
                drawCircle((100..700).random().toFloat(), (100..1100).random().toFloat(), 2f, paint)
            }
        }
        
        player = Bitmap.createBitmap(60, 60, Bitmap.Config.ARGB_8888)
        Canvas(player).apply {
            val p = Paint().apply {
                color = Color.GREEN
                style = Paint.Style.FILL
            }
            drawRect(0f, 0f, 60f, 60f, p)
            p.color = Color.BLUE
            drawRect(20f, 10f, 40f, 40f, p)
        }
        
        enemy = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888)
        Canvas(enemy).apply {
            val p = Paint().apply {
                color = Color.RED
                style = Paint.Style.FILL
            }
            drawRect(0f, 0f, 40f, 40f, p)
        }
        
        paint = Paint()
        thread = Thread(this)
    }
    
    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }
    
    private fun update() {
        // Spawn enemies
        if ((0..100).random() > 98) {
            enemies.add(RectF((0..760).random().toFloat(), -100f, (0..760).random().toFloat() + 40f, -60f))
        }
        
        // Update enemies
        val iterator = enemies.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()
            enemy.offset(0f, 15f)
            
            // Check collision with player
            if (enemy.intersect(playerX, playerY, playerX + 60, playerY + 60)) {
                isPlaying = false
                return
            }
            
            // Remove off-screen enemies
            if (enemy.top > height) {
                iterator.remove()
            }
        }
        
        // Update bullets
        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            bullet.offset(0f, -25f)
            
            // Check bullet-enemy collision
            val enemyIterator = enemies.iterator()
            while (enemyIterator.hasNext()) {
                val enemy = enemyIterator.next()
                if (bullet.intersect(enemy)) {
                    bulletIterator.remove()
                    enemyIterator.remove()
                    score += 100
                    break
                }
            }
            
            // Remove off-screen bullets
            if (bullet.bottom < 0) {
                bulletIterator.remove()
            }
        }
    }
    
    private fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas?.drawBitmap(background, null, Rect(0, 0, width, height), null)
            
            // Draw player
            canvas?.drawBitmap(player, playerX, playerY, null)
            
            // Draw enemies
            for (enemy in enemies) {
                canvas?.drawBitmap(this.enemy, enemy.left, enemy.top, null)
            }
            
            // Draw bullets
            for (bullet in bullets) {
                paint.color = Color.YELLOW
                canvas?.drawRect(bullet, paint)
            }
            
            // Draw score
            paint.textSize = 50f
            paint.color = Color.WHITE
            canvas?.drawText("Score: $score", 20f, 60f, paint)
            
            holder.unlockCanvasAndPost(canvas)
        }
    }
    
    private fun sleep() {
        try {
            Thread.sleep(17) // ~60 FPS
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
    
    fun resume() {
        isPlaying = true
        playerX = (width / 2 - 30).toFloat()
        playerY = (height - 200).toFloat()
        thread.start()
    }
    
    fun pause() {
        try {
            isPlaying = false
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                playerX = event.x - 30
                playerY = event.y - 30
            }
            MotionEvent.ACTION_DOWN -> {
                // Shoot bullet
                bullets.add(RectF(playerX + 25, playerY - 20, playerX + 35, playerY))
            }
        }
        return true
    }
}