# AI Usage Log - Technical Leader

## Student Information

- **Name:** Carlos Eduardo Camelo Montaño
- **Role:** Technical Leader
- **Team:** Entropy
- **Project:** GameZone Unicesar
- **Course:** Programación III

## AI Tool

- **Tool:** ChatGPT
- **Provider:** OpenAI
- **Model:** GPT-5.6 Luna

## Purpose

I used ChatGPT as a technical support and review tool during the development
and integration of GameZone Unicesar.

My work consisted of writing and modifying the code and documentation in
IntelliJ, executing Git and Maven commands myself, testing the application,
and making the final technical decisions. ChatGPT was used to clarify
requirements, review code, analyze errors, verify Pull Requests, explain
Git operations, and review documentation against the actual project state.

All important suggestions were compared against the real repository, the
integration requirement, and the current implementation before being
accepted.

---

## Requirement 5 - System Integration

### 1. Integration Planning and Role Assignment

- **Date:** 2026-09-27
- **Tool:** ChatGPT
- **Phase and branch:** Requirement 5 planning · `develop`
- **Objective:** Understand the integration phases, responsibilities, and the work assigned to each team member.
- **Query:** Asked which integration tasks belonged to Jesús, Daniel, and me as Technical Leader, and whether the work could progress in parallel without creating dependencies between unfinished phases.
- **Response:** ChatGPT explained the phase order and identified A1 and A5 as Developer 1 responsibilities, A2, A4, A6 and A7 as Developer 2 responsibilities, and A3, A8 and A9 as Technical Leader responsibilities.
- **Decision:** I used the requirement-based task distribution to organize the team's work and avoided taking over tasks assigned to the other developers.
- **Related commit:** N/A - planning interaction.

### 2. A3 - Review of the Unified Sale Registration Flow

- **Date:** 2026-09-27
- **Tool:** ChatGPT
- **Phase and branch:** Phase 3 · `refactor/unified-sale-registration`
- **Objective:** Verify that the unified sale flow correctly integrated products, accessories, promotions, and warranties before opening the Pull Request.
- **Query:** Asked for a technical review of `SaleService.registerSale`, the sale calculation, promotion application, warranty assignment, stock handling, and receipt generation.
- **Response:** The review focused on validating stock before processing the sale, resolving products and accessories through their services, selecting the best promotion, assigning basic and extended warranties, calculating the final total, updating inventory, and generating the receipt.
- **Decision:** I kept the implementation and performed the final code and compilation checks myself. The branch was kept clean before creating the Pull Request.
- **Related commit:** `db79493e` - `refactor: unify sale registration flow`

### 3. A3 - Preparation and Verification of PR #25

- **Date:** 2026-09-27
- **Tool:** ChatGPT
- **Phase and branch:** Phase 3 · `refactor/unified-sale-registration`
- **Objective:** Confirm that A3 was ready for cross-review and that no unintended changes or test data remained in the branch.
- **Query:** Asked what should be checked before opening the Pull Request after pushing the final A3 commit.
- **Response:** The review recommended running Maven compilation, checking Git status, and verifying that test data had not introduced unintended tracked changes.
- **Decision:** I ran the verification commands myself, confirmed a clean working tree, and created PR #25 targeting `develop`.
- **Related commit:** `db79493e` - `refactor: unify sale registration flow`

### 4. PR #26 - Review of A4 Accessory Stock Restoration

- **Date:** 2026-09-28
- **Tool:** ChatGPT
- **Phase and branch:** Phase 4 · `feature/return-stock-integration`
- **Objective:** Review the accessory stock restoration integration while keeping the responsibilities of Developer 2 separate from the Technical Leader's work.
- **Query:** Asked whether the A4 implementation correctly restored accessory stock through `AccessoryService` and whether the integration respected the existing service structure.
- **Response:** The review checked the use of `AccessoryService.restoreStock(...)` and the responsibility of `ReturnService` to restore accessories separately from regular products.
- **Decision:** I reviewed the implementation without modifying Daniel's branch and treated the Pull Request as a cross-review task.
- **Related commit:** `14f5b30` - merged A4 implementation.

### 5. PR #28 - Review of A5 Discounted Return Refund

- **Date:** 2026-09-28
- **Tool:** ChatGPT
- **Phase and branch:** Phase 4 · `fix/return-discounted-refund`
- **Objective:** Verify that the proportional refund implemented by Developer 1 respected the original sale discount.
- **Query:** Asked for a detailed review of PR #28, including the refund formula, return receipt, responsibility boundaries, and interaction with extended warranties.
- **Response:** The review confirmed that the refund should use the proportional discount from the original sale and that the extended warranty refund belonged to A7 rather than A5.
- **Decision:** I reviewed the Pull Request as Technical Leader and kept the A5 and A7 responsibilities separate.
- **Related commit:** `1072a3d` - `fix(model): proportional discounted refund and detailed return receipt (A5)`

### 6. PR #29 - Review of A6 Monthly Balance

- **Date:** 2026-09-28
- **Tool:** ChatGPT
- **Phase and branch:** Phase 4 · `fix/monthly-balance-report`
- **Objective:** Verify that monthly sales used the final sale total and that the service exposed the three required monthly values.
- **Query:** Asked for a review of PR #29 against A6, including sales, returns, net balance, promotions, and extended warranty costs.
- **Response:** The review confirmed that monthly sales must use `Sale.calculateFinalTotal()` and that the service should expose total sales, total returns, and the resulting balance separately.
- **Decision:** I reviewed the service-level implementation as consistent with A6 and identified the user-interface integration as my responsibility as Technical Leader.
- **Related commit:** `192b7a3` - `fix(service): calculate monthly sales using the final total...`

### 7. PR #30 - Review of A7 Warranty Cancellation

- **Date:** 2026-09-28
- **Tool:** ChatGPT
- **Phase and branch:** Phase 4 · `feature/return-warranty-cancellation`
- **Objective:** Review the cancellation of warranties when returning a console and verify that the implementation stayed within Developer 2's assigned responsibility.
- **Query:** Asked for a careful review of PR #30, specifically checking `WarrantyService`, `ReturnService`, `Return`, warranty refunds, and role responsibilities.
- **Response:** The review confirmed that `cancelWarranties(productId, saleId)` removes the related warranties, returns the refundable additional warranty cost, and that the amount is incorporated into the return.
- **Decision:** I reviewed the implementation as consistent with A7 and documented the persistence limitation separately from the core A7 behavior.
- **Related commit:** `0daa30d` - `feat(service): cancel console warranties on return...`

---

## A8 - Integration Documentation

### 8. A8 Planning and Documentation Structure

- **Date:** 2026-09-28
- **Tool:** ChatGPT
- **Phase and branch:** Phase 5 · `docs/integration-documentation`
- **Objective:** Determine the complete documentation required for A8 and organize the documentation branch.
- **Query:** Asked which documents A8 required and how the module documentation, integrated diagrams, layers diagram, README, and AI usage logs should be organized.
- **Response:** The review identified the integration analysis, integrated class diagram, layers diagram, README, and the required module-level analysis and class-diagram files as part of the final documentation.
- **Decision:** I created and organized the documentation branch and kept the final documentation aligned with the repository structure.
- **Related commit:** `46ba628` - `docs: add integration analysis`

### 9. A8 - Documentation Review Against the Actual Repository

- **Date:** 2026-09-29
- **Tool:** ChatGPT
- **Phase and branch:** Phase 5 · `docs/integration-documentation`
- **Objective:** Review the documentation against the actual codebase and identify inconsistencies before finalizing PR #32.
- **Query:** Asked to compare the integration documentation with the current implementation and the review comments received on PR #32.
- **Response:** The review identified that A1 needed to explicitly document the current accessory fallback, A6 needed to state that the monthly balance UI was already integrated, and the README needed to reflect the documentation files actually present in the repository.
- **Decision:** I corrected the documentation to describe the current implementation instead of hiding known limitations or outdated statements.
- **Related commit:** `62628c2` - `feat: integrate monthly balance reporting`

### 10. Git Rebase and Conflict Resolution During A8

- **Date:** 2026-09-29
- **Tool:** ChatGPT
- **Phase and branch:** Phase 5 · `docs/integration-documentation`
- **Objective:** Recover the documentation branch after the remote branch had advanced and resolve the resulting rebase conflict.
- **Query:** Asked for guidance after `git push` was rejected because the remote branch contained newer commits and a conflict appeared during rebase.
- **Response:** The guidance explained how to fetch the remote branch, rebase the local work onto it, resolve the conflict in `docs/integration-analysis.md`, stage the resolved file, continue the rebase, and push the resulting branch.
- **Decision:** I performed the Git commands manually, kept the corrected A6 wording, completed the rebase, and pushed the updated branch.
- **Related commit:** `62628c2` - `feat: integrate monthly balance reporting`

### 11. A8 - Adding the Missing Module Documentation

- **Date:** 2026-09-29
- **Tool:** ChatGPT
- **Phase and branch:** Phase 5 · `docs/integration-documentation`
- **Objective:** Complete the documentation files required by Section 8 and align the README structure with the actual repository.
- **Query:** Asked which documentation files were missing from the integration branch and how they should be documented.
- **Response:** The review identified the missing promotion, return, and warranty analysis and class-diagram files required by the integration specification.
- **Decision:** I created the missing documentation files, updated the README and integration analysis, verified the changes with `git diff --check`, and committed the documentation set.
- **Related commit:** `f129151` - `docs: complete integration documentation`

### 12. A8 - Final Technical Review of Module Documentation

- **Date:** 2026-09-29
- **Tool:** ChatGPT
- **Phase and branch:** Phase 5 · `docs/integration-documentation`
- **Objective:** Verify that the newly added promotion, return, and warranty documents accurately represented the current Java implementation.
- **Query:** Asked for technical reviews of `promotion-analysis.md`, `promotion-class-diagram.md`, `return-analysis.md`, `return-class-diagram.md`, `warranty-analysis.md`, and `warranty-class-diagram.md`.
- **Response:** The review compared the documentation with the actual model, service, and repository classes and identified outdated or inaccurate relationships, especially in the warranty and return diagrams.
- **Decision:** I corrected the documentation without modifying the underlying implementation, then verified the resulting staged changes with `git diff --cached --check`.
- **Related commit:** `550d5e8` - `docs: complete integration documentation`

### 13. README Finalization

- **Date:** 2026-09-29
- **Tool:** ChatGPT
- **Phase and branch:** Phase 5 · `docs/integration-documentation`
- **Objective:** Ensure that the README accurately represented the integrated system and listed the required documentation.
- **Query:** Asked for a complete final version of the README containing the integrated functionality, architecture, persistence, project structure, execution, version control, and documentation.
- **Response:** The final review reorganized the README in English, added the required documentation tree, documented the integrated monthly balance UI, and removed outdated references.
- **Decision:** I replaced the README content in IntelliJ and verified it with `git diff --check`.
- **Related commit:** `550d5e8` - `docs: complete integration documentation`

### 14. PR #31 - Review of Developer 2 AI Usage Log

- **Date:** 2026-09-29
- **Tool:** ChatGPT
- **Phase and branch:** Documentation review · `docs/integration-documentation`
- **Objective:** Review Developer 2's AI usage log against Section 11 of the integration requirement.
- **Query:** Asked whether PR #31 contained the required documentation fields for each AI interaction.
- **Response:** The review compared the log with the required eight fields and confirmed that the updated Requirement 5 entries explicitly included Date, Tool, Phase and branch, Objective, Query, Response, Decision, and Related commit.
- **Decision:** I reviewed the updated PR and identified a minor point requiring confirmation about the branch naming used in the A4 entry.
- **Related commit:** `134182e` - current head of PR #31.

### 15. Review of Developer 1 AI Usage Log

- **Date:** 2026-09-29
- **Tool:** ChatGPT
- **Phase and branch:** Documentation review · `docs/ai-usage/developer1-ai-log.md`
- **Objective:** Check whether Developer 1's AI usage log complied with the eight required fields and remained consistent with the real project history.
- **Query:** Asked for a detailed evaluation of Developer 1's updated AI usage log against Section 11 and the actual project history.
- **Response:** The review identified that the log contains detailed technical evidence, but older entries do not explicitly expose all eight required fields. It also identified an inconsistency between the A1 description in the log and the current `CategoryDiscount` implementation.
- **Decision:** I decided that the log should be corrected before considering it fully compliant, without inventing missing interactions or changing historical information that cannot be verified.
- **Related commit:** N/A - documentation review only.

### 16. Final A8 Documentation Commit and Verification

- **Date:** 2026-09-29
- **Tool:** ChatGPT
- **Phase and branch:** Phase 5 · `docs/integration-documentation`
- **Objective:** Verify that the final documentation changes were cleanly committed and pushed before waiting for review.
- **Query:** Asked which files should be staged and how to verify the final documentation commit before pushing it to GitHub.
- **Response:** The guidance recommended staging only the intended documentation files, checking the staged diff for whitespace errors, committing the documentation changes, and pushing the branch.
- **Decision:** I staged only the intended documentation files, confirmed `git diff --cached --check` was clean, created the final documentation commit, and pushed it successfully.
- **Related commit:** `550d5e8` - `docs: complete integration documentation`

### 17. PR #32 Review Corrections

- **Date:** 2026-09-29
- **Tool:** ChatGPT
- **Phase and branch:** Phase 5 · `docs/integration-documentation`
- **Objective:** Address the documentation corrections identified during the PR #32 review.
- **Query:** Asked how to correct the Markdown code fences in the promotion and warranty documentation and how to handle the remaining A1 issue without mixing responsibilities.
- **Response:** The review identified that `promotion-class-diagram.md` required a closing Mermaid fence and `warranty-analysis.md` contained unnecessary Markdown code fences. The A1 `CategoryDiscount` implementation remained a separate module responsibility.
- **Decision:** I corrected the two documentation files, verified them with `git diff --check`, and kept the A1 implementation outside the documentation PR. The A1 limitation remains documented as a pending improvement for a future Pull Request by the responsible developer.
- **Related commit:** `2ca97b7` - `docs: fix markdown code fences`

### 18. Synchronization with the Updated Develop Branch

- **Date:** 2026-09-29
- **Tool:** ChatGPT
- **Phase and branch:** Phase 5 · `docs/integration-documentation`
- **Objective:** Update the documentation branch after Developer 2's AI usage log was merged into `develop`.
- **Query:** Asked how to synchronize the PR #32 branch with the latest `develop` without losing the existing documentation work.
- **Response:** The guidance recommended fetching `origin/develop`, merging it into the documentation branch, checking the merge result, and then pushing the updated branch.
- **Decision:** I executed the Git commands myself, completed the merge without conflicts, verified the working tree, and pushed the synchronized branch to GitHub.
- **Related commit:** `adad804` - `Merge remote-tracking branch 'origin/develop' into docs/integration-documentation`

---

## General Conclusion

Throughout Requirement 5, I used ChatGPT mainly as a technical tutor,
code reviewer, Git guide, Pull Request reviewer, and documentation reviewer.

I wrote and modified the project files in IntelliJ, executed Git and Maven
commands myself, performed the required tests, and made the final decisions
about which suggestions to accept or reject.

The most significant uses of AI were related to:

- understanding the integration phases and team responsibilities;
- reviewing the unified sale flow in A3;
- reviewing A4-A7 Pull Requests;
- verifying monthly balance integration;
- analyzing Git conflicts and rebases;
- checking documentation against the actual implementation;
- completing the A8 documentation;
- reviewing the team's AI usage logs against the integration requirement;
- synchronizing the documentation branch with the updated `develop` branch.

The final decisions were based on the actual repository state, the
integration requirement, compiler results, application tests, Pull Request
reviews, and the team's documented responsibilities.