ALTER TABLE payments
    ADD COLUMN checkout_session_id VARCHAR(255);

ALTER TABLE payments
    ADD COLUMN checkout_url VARCHAR(1000);

UPDATE payments
SET checkout_session_id = provider_payment_id,
    checkout_url = 'legacy'
WHERE checkout_session_id IS NULL;

ALTER TABLE payments
    ALTER COLUMN checkout_session_id SET NOT NULL;

ALTER TABLE payments
    ALTER COLUMN checkout_url SET NOT NULL;

ALTER TABLE payments
    ADD CONSTRAINT uk_payments_checkout_session_id
        UNIQUE (checkout_session_id);