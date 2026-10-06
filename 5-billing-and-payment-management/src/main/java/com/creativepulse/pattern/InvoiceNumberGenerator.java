package com.creativepulse.pattern;

import java.time.Year;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DESIGN PATTERN 1 - SINGLETON.
 *
 * Only ONE instance of this class can ever exist in the application, so every
 * invoice number is produced by the same counter and duplicates are impossible.
 * Uses the thread-safe "Bill Pugh / holder" idiom.
 */
public class InvoiceNumberGenerator {

    private final AtomicInteger counter = new AtomicInteger(0);

    // private constructor - nobody outside can call "new InvoiceNumberGenerator()"
    private InvoiceNumberGenerator() { }

    private static class Holder {
        private static final InvoiceNumberGenerator INSTANCE = new InvoiceNumberGenerator();
    }

    public static InvoiceNumberGenerator getInstance() {
        return Holder.INSTANCE;
    }

    /** Called once when the app starts so numbering continues after a restart. */
    public void syncWith(long existingInvoiceCount) {
        counter.set((int) existingInvoiceCount);
    }

    /** Produces numbers such as INV-2026-0001, INV-2026-0002 ... */
    public String next() {
        return String.format("INV-%d-%04d", Year.now().getValue(), counter.incrementAndGet());
    }
}
