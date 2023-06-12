package bph.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import java.util.ArrayList;
import java.util.Collections;

public class HallOfFame {
    private static final String FILE_PATH = "data/scores.txt";
    private static final int NUM_TOP_SCORES = 3;

    // Lecture des scores à partir du fichier
    static List<PlayerScore> readScoresFromFile() {
        List<PlayerScore> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    PlayerScore playerScore = new PlayerScore(name, score);
                    scores.add(playerScore);
                }
            }
        } catch (IOException e) {
        	System.out.println("Erreur dans la lecture du fichier");
        }
        return scores;
    }

    // Récupération des trois meilleurs scores
    static List<PlayerScore> getTopScores(List<PlayerScore> scores) {
    	Objects.requireNonNull(scores);
    	scores.sort(Collections.reverseOrder());
        return scores.subList(0, Math.min(scores.size(), NUM_TOP_SCORES));
    }
    
    // Vérifie si le nom du joueur existe déjà dans la liste des scores et donne son index
    static int getPlayerIndex(List<PlayerScore> scores, String name) {
        Objects.requireNonNull(scores);
        Objects.requireNonNull(name);
        for (int i = 0; i < scores.size(); i++) {
            PlayerScore score = scores.get(i);
            if (score.getName().equals(name)) {
                return i;
            }
        }
        return -1; // Joueur non trouvé
    }

    // Ajout d'un nouveau joueur et son score
    static void addNewPlayerScore(List<PlayerScore> scores, String name, int score) {
    	PlayerScore playerScore = new PlayerScore(name, score);
    	int index = getPlayerIndex(scores, name);
    	if (index != -1) {
            PlayerScore existingPlayerScore = scores.get(index);
            if (playerScore.getScore() > existingPlayerScore.getScore()) {
                existingPlayerScore.setScore(playerScore.getScore());
                updateScoresToFile(scores);
                //System.out.println("Le nom du joueur existe déjà, nous avons pris le score le plus haut parmi l'ancien et le nouveau");
            }
        } else {
            scores.add(playerScore);
            writeScoresToFile(playerScore);
            //System.out.println("Nouveau joueur ajouté : " + name + " - " + score);
        }
    }

    // Écriture des scores dans le fichier
    static void writeScoresToFile(PlayerScore playerScore) {
    	Objects.requireNonNull(playerScore);
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(playerScore.getName() + "," + playerScore.getScore());
        } catch (IOException e) {
            System.out.println("Erreur dans l'écriture du fichier");
        }
    }
    
    static void updateScoresToFile(List<PlayerScore> scores) {
        Objects.requireNonNull(scores);
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (PlayerScore playerScore : scores) {
                writer.println(playerScore.getName() + "," + playerScore.getScore());
            }
        } catch (IOException e) {
            System.out.println("Erreur dans l'écriture du fichier");
        }
    }
}