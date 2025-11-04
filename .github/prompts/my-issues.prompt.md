---
mode: 'agent'
tools: ['github/get_label', 'github/get_me', 'github/issue_read', 'github/list_issues', 'github/search_issues', 'githubRepo']
description: 'List my Github issues in the current repository'
model: Auto (copilot)
---

Search the current repo (using #githubRepo for the repo info) and list any issues you find (using #github/list_issues) that are assigned to me (using #github/get_me).

Suggest issues that I might want to focus on based on their age, the amount of comments, and their status (open/closed).
