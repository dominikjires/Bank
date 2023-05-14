package com.jires.Bank.app.repository;

import com.jires.Bank.app.domain.ConfirmationToken;
import com.jires.Bank.app.domain.User;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.jires.Bank.app.repository.UserRepository.findUser;

@Repository
public class ConfirmationTokenRepository {

    private static final String FILENAME = "data/tokens.txt";

    // Finds the confirmation token by token string.
    public Optional<ConfirmationToken> findByToken(String token) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equals(token)) {
                    Long id = Long.valueOf(data[0]);
                    LocalDateTime createdAt = LocalDateTime.parse(data[2]);
                    LocalDateTime expiresAt = LocalDateTime.parse(data[3]);
                    Boolean confirmed = Boolean.parseBoolean(data[4]);
                    User user = findUser(id);
                    return Optional.of(new ConfirmationToken(token, createdAt, expiresAt,confirmed, id));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Updates the confirmed timestamp of a confirmation token.
    public int updateConfirmedAt(String token, LocalDateTime confirmedAt) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            StringBuilder sb = new StringBuilder();
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equals(token)) {
                    data[4] = confirmedAt == null ? "" : confirmedAt.toString();
                    found = true;
                }
                sb.append(String.join(",", data)).append("\n");
            }
            br.close();
            if (found) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {
                    bw.write(sb.toString());
                    bw.close();
                }
                return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Gets the confirmation token by token string.
    public Optional<ConfirmationToken> getToken(String token) {
        try {
            File file = new File("data/tokens.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokenData = line.split(",");
                if (tokenData[0].equals(token)) {
                    ConfirmationToken confirmationToken = new ConfirmationToken(tokenData[0], LocalDateTime.parse(tokenData[1]), LocalDateTime.parse(tokenData[2]),Boolean.parseBoolean(tokenData[3]),  Long.valueOf(tokenData[4]));
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
}

