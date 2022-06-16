package com.pedrocosta.exchanglog.test;

import com.pedrocosta.springutils.output.Messages;
import com.pedrocosta.springutils.output.utils.LocaleUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagesTest {
    @Test
    public void test_getMessage_default() {
        assertEquals("Total saved: test",
                Messages.get("total.saved", "test"));
    }

    @Test
    public void test_getMessage_ptBR() {
        assertEquals("Total gravado: teste",
                Messages.get(LocaleUtils.fromString("pt_BR"), "total.saved", "teste"));
    }
}
