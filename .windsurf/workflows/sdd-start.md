---
description: Start SDD session for Wishlot — ensures strict spec-first workflow
---

# SDD Start Workflow

**INVOKE THIS at the beginning of every session with:** `/sdd-start`

This workflow forces the following hard stops before any code changes:

1. Read `.windsurf/rules/spec-driven.mdc`
2. Check for existing pending files in `docs/changes/pending/`
3. If user request implies code changes → create `docs/changes/pending/<feature>.md`
4. STOP. Show pending file to user. Wait for explicit "ok" or corrections.
5. Only after approval → update main specs (`docs/*.md`), delete pending, then code.

**Critical rules:**
- NEVER write code before pending approval.
- NEVER update `docs/*.md` before pending approval.
- NEVER skip the approval step for "simple" or "quick" changes.
- If user says "do it now" without approving pending → STOP and ask for explicit "ok".
