package config.spawn;

import java.util.Random;
/**
 * 
 * @author bnk
 */
public class SpawnManager {
    private int[] spawnPointX; // Width
    private int[] spawnPointY; // Height
    private int spawnInterval; // Time in milliseconds
    private int maxMonsters; // Count
    private int minDistancePlayer;
    private long lastSpawnTime;
    private Random random;
    
    public SpawnManager(int spawnPointX[], int spawnPointY[], int spawnInterval, int maxMonsters) {
        this.spawnPointX = spawnPointX;
        this.spawnPointY = spawnPointY;
        this.spawnInterval = spawnInterval;
        this.maxMonsters = maxMonsters;
        this.minDistancePlayer = 100; // Distância mínima do player
        this.lastSpawnTime = System.currentTimeMillis();
        this.random = new Random();
    }

    public boolean shouldSpawn(int monstersInScreen) {
        long currentTime = System.currentTimeMillis();
        
        // Verifica se atingiu o limite máximo
        if (monstersInScreen >= maxMonsters) {
            return false;
        }
        
        // Verifica se passou o tempo necessário desde o último spawn
        if (currentTime - lastSpawnTime >= spawnInterval) {
            lastSpawnTime = currentTime;
            return true;
        }
        
        return false;
    }
    
    public int[] getRandomSpawnPosition(double playerX, double playerY, int screenWidth, int screenHeight) {
        int[] position = new int[2];
        int attempts = 0;
        int maxAttempts = 10;
        
        do {
            // Escolhe um ponto de spawn aleatório dos pontos definidos
            int spawnIndex = random.nextInt(spawnPointX.length);
            position[0] = spawnPointX[spawnIndex];
            position[1] = spawnPointY[spawnIndex];
            
            // Se não há pontos definidos, gera posição aleatória nas bordas
            if (spawnPointX.length == 0) {
                position = generateRandomBorderPosition(screenWidth, screenHeight);
            }
            
            attempts++;
        } while (isTooCloseToPlayer(position[0], position[1], playerX, playerY) && attempts < maxAttempts);
        
        return position;
    }
    
    private int[] generateRandomBorderPosition(int screenWidth, int screenHeight) {
        int[] position = new int[2];
        int side = random.nextInt(4); // 0=top, 1=right, 2=bottom, 3=left
        
        switch (side) {
            case 0: // Top
                position[0] = random.nextInt(screenWidth);
                position[1] = 0;
                break;
            case 1: // Right
                position[0] = screenWidth;
                position[1] = random.nextInt(screenHeight);
                break;
            case 2: // Bottom
                position[0] = random.nextInt(screenWidth);
                position[1] = screenHeight;
                break;
            case 3: // Left
                position[0] = 0;
                position[1] = random.nextInt(screenHeight);
                break;
        }
        
        return position;
    }
    
    private boolean isTooCloseToPlayer(int x, int y, double playerX, double playerY) {
        double distance = Math.sqrt(Math.pow(x - playerX, 2) + Math.pow(y - playerY, 2));
        return distance < minDistancePlayer;
    }

    // -- Getters --
    public int[] getSpawnPointX() { return spawnPointX; }
    public int[] getSpawnPointY() { return spawnPointY; }
    public int getSpawnInterval() { return spawnInterval; }
    public int getMaxMonsters() { return maxMonsters; }
    public int getMinDistancePlayer() { return minDistancePlayer; }

    // -- Setters --
    public void setSpawnPointX(int[] spawnPointX) { this.spawnPointX = spawnPointX; }
    public void setSpawnPointY(int[] spawnPointY) { this.spawnPointY = spawnPointY; }
    public void setSpawnInterval(int spawnInterval) { this.spawnInterval = spawnInterval; }
    public void setMaxMonsters(int maxMonsters) { this.maxMonsters = maxMonsters; }
    public void setMinDistancePlayer(int minDistancePlayer) { this.minDistancePlayer = minDistancePlayer; }
}