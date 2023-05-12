package com.jires.Bank.app.service;
import com.jires.Bank.app.domain.ConfirmationToken;
import com.jires.Bank.app.repository.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
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
                    + token.getExpiresAt() + "," + token.getConfirmed() + ","
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
                    ConfirmationToken confirmationToken = new ConfirmationToken(tokenData[0], LocalDateTime.parse(tokenData[1]), LocalDateTime.parse(tokenData[2]), Boolean.parseBoolean(tokenData[3]), Long.valueOf(tokenData[4]));
                    bufferedReader.close();
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
            File tempFile = new File("data/tokens_temp.txt");

            BufferedReader reader1 = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer1 = new BufferedWriter(new FileWriter(tempFile));
            Boolean confirmed;
            String line;
            int rowsAffected = 0;

            while ((line = reader1.readLine()) != null) {
                String[] tokenData = line.split(",");
                if (tokenData[0].equals(token)) {
                    if (LocalDateTime.now().isAfter(LocalDateTime.parse(tokenData[1])) && LocalDateTime.now().isBefore(LocalDateTime.parse(tokenData[2]))) {
                        confirmed = true;
                    } else {
                        confirmed = false;
                    }
                    writer1.write(tokenData[0] + "," + tokenData[1] + ","
                            + tokenData[2] + "," + confirmed + ","
                            + tokenData[4]);
                    writer1.newLine();
                    rowsAffected++;
                } else {
                    writer1.write(line);
                    writer1.newLine();
                }
            }

            writer1.close();
            reader1.close();

            //Error deleting file
            if (inputFile.delete()) {
                if (!tempFile.renameTo(inputFile)) {
                    System.err.println("Error renaming file");
                    return 0;
                }
            } else {
                System.err.println("Error deleting file");
                return 0;
            }

            return rowsAffected;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isTokenConfirmed(String token) {
        Optional<ConfirmationToken> optionalToken = confirmationTokenRepository.getToken(token);
        if (optionalToken.isPresent()) {
            ConfirmationToken confirmationToken = optionalToken.get();
            return confirmationToken.getConfirmed();
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        ConfirmationTokenRepository confirmationTokenRepository = new ConfirmationTokenRepository();
        ConfirmationTokenService confirmationTokenService = new ConfirmationTokenService(confirmationTokenRepository);

        boolean isConfirmed = confirmationTokenService.isTokenConfirmed("220f96a6-a280-4bbb-97c8-da8e94844144");

        System.out.println("Token is confirmed: " + isConfirmed);
    }

}
