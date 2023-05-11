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

    public Optional<ConfirmationToken> findByToken(String token) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equals(token)) {
                    Long id = Long.valueOf(data[0]);
                    LocalDateTime createdAt = LocalDateTime.parse(data[2]);
                    LocalDateTime expiresAt = LocalDateTime.parse(data[3]);
                    LocalDateTime confirmedAt = data[4].isEmpty() ? null : LocalDateTime.parse(data[4]);
                    User user = findUser(id);
                    return Optional.of(new ConfirmationToken(token, createdAt, expiresAt, id));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

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

}


