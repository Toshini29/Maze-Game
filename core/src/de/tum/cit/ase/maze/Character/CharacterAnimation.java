package de.tum.cit.ase.maze.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * The `CharacterAnimation` class handles the animation of the game character.
 * It includes animations for different directions (up, down, left, right) and manages the character's position.
 */
public class CharacterAnimation {
    private Vector2 position;
    private Animation<TextureRegion> upAnimation, downAnimation, leftAnimation, rightAnimation;
    private float stateTime;
    private Character.Direction currentDirection = Character.Direction.DOWN; // Default direction

    /**
     * Creates a new character animation with default values.
     */
    public CharacterAnimation() {
        position = new Vector2();
        stateTime = 0f;
        loadCharacterAnimation();
    }

    /**
     * Loads the character animations from the specified texture sheet.
     * Each direction animation is assumed to have 4 frames.
     */
    private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        // Corrected mapping: down, right, left, up.
        downAnimation = createAnimation(walkSheet, 0 * frameHeight, frameWidth, frameHeight, animationFrames);
        rightAnimation = createAnimation(walkSheet, 1 * frameHeight, frameWidth, frameHeight, animationFrames);
        upAnimation = createAnimation(walkSheet, 2 * frameHeight, frameWidth, frameHeight, animationFrames);
        leftAnimation = createAnimation(walkSheet, 3 * frameHeight, frameWidth, frameHeight, animationFrames);
    }

    /**
     * Creates an animation from the given texture sheet.
     *
     * @param sheet       The texture sheet containing animation frames.
     * @param startY      The starting Y-coordinate of animation frames in the sheet.
     * @param frameWidth  The width of each animation frame.
     * @param frameHeight The height of each animation frame.
     * @param frameCount  The number of frames in the animation.
     * @return The created animation.
     */
    private Animation<TextureRegion> createAnimation(Texture sheet, int startY, int frameWidth, int frameHeight, int frameCount) {
        Array<TextureRegion> frames = new Array<>(TextureRegion.class);
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(sheet, i * frameWidth, startY, frameWidth, frameHeight));
        }
        return new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
    }

    /**
     * Updates the character animation's state time.
     *
     * @param delta The time elapsed since the last frame.
     */
    public void update(float delta) {
        // Update the state time - could also be done in the GameScreen
        stateTime += delta;
    }

    /**
     * Renders the character animation based on the character's direction and position.
     *
     * @param batch     The SpriteBatch used for rendering.
     * @param direction The current direction of the character.
     * @param x         The X-coordinate of the character's position.
     * @param y         The Y-coordinate of the character's position.
     */
    public void render(SpriteBatch batch, Character.Direction direction, float x, float y) {
        TextureRegion currentFrame = null;
        switch (direction) {
            case UP:
                currentFrame = upAnimation.getKeyFrame(stateTime, true);
                break;
            case DOWN:
                currentFrame = downAnimation.getKeyFrame(stateTime, true);
                break;
            case LEFT:
                currentFrame = leftAnimation.getKeyFrame(stateTime, true);
                break;
            case RIGHT:
                currentFrame = rightAnimation.getKeyFrame(stateTime, true);
                break;
        }
        if (currentFrame != null) {
            batch.draw(currentFrame, x, y);
        }
    }

}
