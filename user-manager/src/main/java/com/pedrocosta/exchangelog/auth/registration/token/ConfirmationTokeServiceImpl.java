package com.pedrocosta.exchangelog.auth.registration.token;

import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmationTokeServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository repository;

    public ConfirmationTokeServiceImpl(ConfirmationTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public ConfirmationToken save(ConfirmationToken obj) throws SaveDataException {
        return repository.save(obj);
    }

    @Override
    public ConfirmationToken find(String token) throws NotFoundException {
        return repository.findByToken(token).orElseThrow(
                () -> new NotFoundException(Messages.get("not.found", "token")));
    }

    @Override
    public void confirm(String token) throws SaveDataException, IllegalArgumentException {
        try {
            ConfirmationToken confirmationToken = this.find(token);
            confirmationToken.setConfirmationTime(LocalDateTime.now());
            this.save(confirmationToken);
        } catch (NotFoundException e) {
            Log.error(this, e);
            throw new IllegalArgumentException(e);
        }
    }
}
