
CREATE TABLE IF NOT EXISTS courseSlotStatus (
    id uuid,
    courseCode text,
    maxSlot bigint,
    currentSlot bigint,
    PRIMARY KEY(id)
);
