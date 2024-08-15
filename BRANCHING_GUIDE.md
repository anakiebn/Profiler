# Branch Naming Guide

## Branch Types

1. **Feature Branches**: For new features or enhancements.
    - Naming Convention: `feature/<feature-name>`
    - Example: `feature/user-authentication`

2. **Bugfix Branches**: For fixing bugs.
    - Naming Convention: `bugfix/<issue-id>-<short-description>`
    - Example: `bugfix/123-login-issue`

3. **Hotfix Branches**: For critical fixes that need to go directly to production.
    - Naming Convention: `hotfix/<issue-id>-<short-description>`
    - Example: `hotfix/456-critical-login-issue`

4. **Release Branches**: For preparing releases.
    - Naming Convention: `release/<version-number>`
    - Example: `release/1.0.0`

5. **Development Branch**: The main branch where development happens.
    - Name: `develop`

6. **Master/Main Branch**: The production-ready code.
    - Name: `main` or `master`

## Additional Guidelines

- **Use lowercase**: All branch names should be in lowercase letters.
- **Use hyphens**: Separate words with hyphens (`-`).
- **Be descriptive**: Use meaningful names that clearly convey the purpose of the branch.
- **Include issue IDs**: When applicable, include the issue or ticket ID in the branch name.

## Examples

- Feature Branch: `feature/user-authentication`
- Bugfix Branch: `bugfix/123-login-issue`
- Hotfix Branch: `hotfix/456-critical-login-issue`
- Release Branch: `release/1.0.0`
- Development Branch: `develop`
- Master/Main Branch: `main`
