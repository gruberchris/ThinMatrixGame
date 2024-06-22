package com.chrisgruber.thinmatrixgame.engine.terrains;

import com.chrisgruber.thinmatrixgame.engine.ModelLoader;
import com.chrisgruber.thinmatrixgame.engine.models.RawModel;
import com.chrisgruber.thinmatrixgame.engine.textures.TerrainTexture;
import com.chrisgruber.thinmatrixgame.engine.textures.TerrainTexturePack;
import com.chrisgruber.thinmatrixgame.engine.utils.Maths;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {
    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 40;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * (float)256;

    private final float x;
    private final float z;
    private final RawModel rawModel;
    private final TerrainTexturePack terrainTexturePack;
    private final TerrainTexture blendMap;
    private float[][] heights;  // height of each vertex on the terrain


    public Terrain(int x, int z, ModelLoader modelLoader, TerrainTexturePack terrainTexturePack, TerrainTexture blendMap, String heightMapFilename) {
        this.x = x * SIZE;
        this.z = z * SIZE;
        this.rawModel = generateTerrain(modelLoader, heightMapFilename);
        this.terrainTexturePack = terrainTexturePack;
        this.blendMap = blendMap;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TerrainTexturePack getTerrainTexturePack() {
        return terrainTexturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public float getHeightOfTerrain(float worldX, float worldZ) {
        // Define the number of sample points and the radius around the entity
        int numSamplePoints = 4;
        float sampleRadius = 0.5f;

        // Initialize the total height to 0
        float totalHeight = 0;

        // Sample the height at several points around the entity
        for (int i = 0; i < numSamplePoints; i++) {
            // Calculate the sample point coordinates
            float sampleX = worldX + (float) Math.cos(2 * Math.PI * i / numSamplePoints) * sampleRadius;
            float sampleZ = worldZ + (float) Math.sin(2 * Math.PI * i / numSamplePoints) * sampleRadius;

            // Calculate the height at the sample point
            float sampleHeight = getSingleHeightOfTerrain(sampleX, sampleZ);

            // Add the sample height to the total height
            totalHeight += sampleHeight;
        }

        // Return the average height
        return totalHeight / numSamplePoints;
    }

    private float getSingleHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / ((float) heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }

        float xCoordinate = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoordinate = (terrainZ % gridSquareSize) / gridSquareSize;
        float triangleHeight;

        if (xCoordinate <= (1 - zCoordinate)) {
            triangleHeight = Maths.calculateTriangleHeightByBarycentric(
                    new Vector3f(0, heights[gridX][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoordinate, zCoordinate)
            );
        } else {
            triangleHeight = Maths.calculateTriangleHeightByBarycentric(
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoordinate, zCoordinate)
            );
        }

        return triangleHeight;
    }

    private RawModel generateTerrain(ModelLoader modelLoader, String heightMapFilename) {
        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ImageIO.read(new File(heightMapFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert bufferedImage != null;

        final int VERTEX_COUNT = bufferedImage.getHeight();   // don't use too big a height map or the terrain will be high poly

        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT)];
        int vertexPointer = 0;

        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = j / ((float) VERTEX_COUNT - 1) * SIZE;
                float height = getHeight(j, i, bufferedImage);
                heights[j][i] = height;
                vertices[vertexPointer * 3 + 1] = height;
                vertices[vertexPointer * 3 + 2] = i / ((float) VERTEX_COUNT - 1) * SIZE;

                Vector3f normal = calculateNormal(j, i, bufferedImage);

                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoords[vertexPointer * 2] = j / ((float) VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }

        int pointer = 0;

        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return modelLoader.loadToVao(vertices, textureCoords, normals, indices);
    }

    private float getHeight(int x, int z,  BufferedImage bufferedImage) {
        if (x < 0 || x >= bufferedImage.getHeight() || z < 0 || z >= bufferedImage.getHeight()) {
            return 0;   // out of bounds
        }

        float height = bufferedImage.getRGB(x, z);
        height += MAX_PIXEL_COLOR / 2f;
        height /= MAX_PIXEL_COLOR / 2f;
        height *= MAX_HEIGHT;
        return height;
    }

    private float getHeightFromRGB(int x, int z, BufferedImage bufferedImage) {
        if (x < 0 || x >= bufferedImage.getWidth() || z < 0 || z >= bufferedImage.getHeight()) {
            return 0;   // out of bounds
        }

        int rgb = bufferedImage.getRGB(x, z);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        // Convert RGB to grayscale using the standard formula
        float grayscale = 0.299f * red + 0.587f * green + 0.114f * blue;

        grayscale += MAX_PIXEL_COLOR / 2f;
        grayscale /= MAX_PIXEL_COLOR / 2f;
        grayscale *= MAX_HEIGHT;

        return grayscale;
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage bufferedImage) {
        float heightL = getHeight(x - 1, z, bufferedImage);
        float heightR = getHeight(x + 1, z, bufferedImage);
        float heightD = getHeight(x, z - 1, bufferedImage);
        float heightU = getHeight(x, z + 1, bufferedImage);
        Vector3f normal= new Vector3f(heightL - heightR, 2.0f, heightD - heightU);
        normal.normalize();
        return normal;
    }
}
