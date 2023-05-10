package com.jires.Bank.app.service;
import com.jires.Bank.app.domain.ConfirmationToken;
import com.jires.Bank.app.repository.ConfirmationTokenRepository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;


public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public static void saveConfirmationToken(ConfirmationToken token) {
        try {
            File file = new File("data/tokens.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(token.getToken() + "," + token.getCreatedAt() + ","
                    + token.getExpiresAt() + "," + token.getConfirmedAt() + ","
                    + token.getId());
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<ConfirmationToken> getToken(String token) {
        try {
            File file = new File("data/tokens.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokenData = line.split(",");
                if (tokenData[0].equals(token)) {
                    ConfirmationToken confirmationToken = new ConfirmationToken(tokenData[0], LocalDateTime.parse(tokenData[1]), LocalDateTime.parse(tokenData[2]),  Long.valueOf(tokenData[4]));
                    return Optional.of(confirmationToken);
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public int setConfirmedAt(String token) {
        try {
            File inputFile = new File("data/tokens.txt");
            File tempFile = new File("data/tempTokens.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            int rowsAffected = 0;

            while ((line = reader.readLine()) != null) {
                String[] tokenData = line.split(",");
                if (tokenData[0].equals(token)) {
                    writer.write(tokenData[0] + "," + tokenData[1] + ","
                            + tokenData[2] + "," + LocalDateTime.now() + ","
                            + tokenData[4]);
                    writer.newLine();
                    rowsAffected++;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

            writer.close();
            reader.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);

            return rowsAffected;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
