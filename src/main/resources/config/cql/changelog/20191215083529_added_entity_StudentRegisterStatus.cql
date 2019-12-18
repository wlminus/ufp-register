
CREATE TABLE IF NOT EXISTS studentRegisterStatus (
    id uuid,
    studentId text,
    isPrior bigint,
    maxRegister bigint,
    currentRegister bigint,
    PRIMARY KEY(id)
);
