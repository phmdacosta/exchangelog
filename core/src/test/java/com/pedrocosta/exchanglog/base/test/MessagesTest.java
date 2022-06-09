package com.pedrocosta.exchanglog.base.test;

import com.pedrocosta.utils.output.Messages;
import org.junit.jupiter.api.Test;

public class MessagesTest {

    @Test
    public void testGetMessage_totalSaved() {
        assert "Total saved: test.".equals(
                Messages.get("total.saved", "test"));
    }
}
