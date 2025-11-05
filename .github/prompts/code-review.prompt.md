---
mode: 'agent'
tools: ['edit', 'runNotebooks', 'search', 'new', 'runCommands', 'runTasks', 'usages', 'vscodeAPI', 'problems', 'changes', 'testFailure', 'openSimpleBrowser', 'fetch', 'githubRepo', 'extensions', 'todos', 'runTests']
description: 'Do a code review of staged files before committing and pushing your changes.'
model: Claude Sonnet 4.5 (copilot)
---

Apply a code review of all staged files. Do not make any changes; report your review results at the end. Respect the following rules for your review:

- Code is commented in hard-to-understand areas
- Corresponding changes to the documentation have been made
- Run the linter to prove the changes generate no new lint errors
- Run unit tests to prove the change does not introduce breaking changes
- Check if any dependent changes have been merged