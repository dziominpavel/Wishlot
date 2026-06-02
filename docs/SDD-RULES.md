# SDD Hard Rules — Wishlot

> These rules are **mandatory** and override any other instruction.

## 1. Spec-First Absolute

- Code changes are **FORBIDDEN** until:
  1. A `docs/changes/pending/<feature>.md` file is created AND
  2. User explicitly approves with "ok" (or similar confirmation)
- "Simple", "quick", or "just fix it" are NOT exemptions.

## 2. Approval Definition

Valid approvals:
- "ok"
- "одобряю"
- "да"
- "approve"

NOT valid (do NOT proceed):
- "ok" in response to a different question
- "let's do it" without reading pending
- Any ambiguous phrasing

## 3. Spec Consistency

- Every feature must have its own spec file in `docs/`.
- No contradictions between `docs/*.md` files.
- If conflict found → STOP and ask user which spec is correct.

## 4. Process for Code Changes

```
User request → Create pending → STOP → User "ok" → Update docs/ → Delete pending → Code → Tests
```

## 5. Consequence of Violation

If code is written before approval:
1. Immediately stop all edits.
2. Revert all code changes (`git checkout`).
3. Return to pending approval step.
4. Do NOT attempt to "fix forward".
