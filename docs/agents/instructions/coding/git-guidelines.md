# Git Usage Guidelines

These guidelines describe the expected Git hygiene for work produced in this repository.

## Branching Model

- use short-lived branches
- keep changes focused and easy to review
- avoid long-running divergence from `main`

If work is incomplete but needs to integrate early, prefer slicing the feature smaller or using a feature flag when relevant.

## Commit Style

Use Conventional Commits when creating commits:

```text
<type>(<scope>): <short description>
```

Common types:
- `feat`
- `fix`
- `docs`
- `refactor`
- `test`
- `build`
- `chore`

Examples:

```text
feat(domain): add place order use case
fix(application): map token validation errors to 400
docs(agent): clarify domain testing expectations
```

## Commit Message Quality

- write the subject in imperative mood
- keep the subject concise
- explain why in the body when the change is not obvious
- reference issues or breaking changes in the footer when needed

## Reverts

When undoing merged work on `main`, prefer a revert commit over history rewriting.

Example:

```bash
git revert <commit-sha>
```

## Expectations for Agents

Agents should:
- avoid destructive git commands unless explicitly requested
- avoid rewriting unrelated user changes
- keep diffs narrow and reviewable
- not mix unrelated cleanup into feature work

If the worktree is dirty, an agent should adapt to that state instead of force-resetting it.