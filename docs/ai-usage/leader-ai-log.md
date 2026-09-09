# AI Usage Log - Technical Leader

## Student Information

- **Name:** Carlos Eduardo Camelo Montaño
- **Role:** Technical Leader
- **Team:** Entropy
- **Project:** GameZone Unicesar
- **Course:** Programación III

## Artificial Intelligence Used

- **Tool:** ChatGPT
- **Provider:** OpenAI
- **Model:** GPT-5.6 Luna

## Purpose of AI Usage

During the development of GameZone Unicesar, I used ChatGPT as a support
tool mainly to clarify doubts, understand Git and Maven, interpret compiler
errors, review code, and help integrate the modules developed by the team.

The AI was not used as a replacement for the development process. I checked
the suggestions against the actual project code, adapted them to the project,
and tested the resulting changes.

## AI Usage Records

### 1. Understanding the workshop and project structure

At the beginning of the project, I used ChatGPT to clarify the requirements
of the workshop, especially the four-layer architecture, the required
packages, the functional operations, and the Git workflow.

This helped me organize my work as Technical Leader and understand how the
different modules had to be integrated.

### 2. Git and GitHub workflow

I used ChatGPT to clarify how to work with `main`, `develop`, and
`feature/*` branches, as well as how Pull Requests and cross-reviews should
be handled.

I also used it when I had doubts about switching branches, synchronizing
`develop`, checking the repository status, and interpreting the Git history.

### 3. Integration of the Product and Person modules

After the other team members developed their modules, I reviewed their code
and used ChatGPT to help me understand the existing classes and identify how
they should be connected with the sales and UI modules.

I verified the suggestions against the actual implementation rather than
assuming that the classes had a specific structure.

### 4. Maven and compiler errors

During the integration process, I used ChatGPT to understand Maven commands
and interpret compilation errors reported by IntelliJ.

The explanations helped me identify incorrect method calls and adjust my
code to the real method signatures in the project.

### 5. Integrating the application in Main and UI

One of my responsibilities was connecting the repositories and services in
`Main` and integrating the different operations into the console interface.

I used ChatGPT to help me organize these dependencies according to the
layered architecture and to avoid having the UI access the persistence layer
directly.

### 6. Product operations in the console

I used ChatGPT as a support tool while implementing the console operations
for registering video games, registering consoles, and listing products.

During this work, I compared the suggested structure with the actual
`ProductService` methods and adapted the implementation when the existing
code differed from what I initially expected.

### 7. Customer and seller operations

I implemented the console interaction for registering customers and listing
customers and sellers.

ChatGPT helped me clarify how the UI should request the information and use
the corresponding service methods without moving business logic into the UI.

### 8. Sales integration

I used ChatGPT to understand how the sales module developed for the project
should be connected to the console.

The main points I reviewed were customer and seller validation, product
selection, stock validation, stock reduction, and persistence of the sale.

I also reviewed the way repeated product IDs were handled so that multiple
units of the same product could be included in one sale.

### 9. Sales history operations

After integrating sale registration, I connected the three sales queries to
the UI:

- complete sales history;
- customer purchase history;
- seller sales history.

I used ChatGPT to clarify how the existing `SaleService` methods should be
called from the console.

### 10. JavaDoc and language requirements

I used ChatGPT to clarify the workshop requirement regarding JavaDoc and the
use of English in source-code documentation.

This helped me maintain English class, method, and JavaDoc documentation
while keeping the user-facing console messages in Spanish.

### 11. Git review and Pull Requests

I used ChatGPT to review my teammates' Pull Requests and understand the
changes before approving them.

For example, while reviewing the sales integration PR, I checked the order
of stock validation and stock reduction in `SaleService`.

I also reviewed Daniel's changes related to CSV validation, duplicate
customer IDs, and JavaDoc before approving his Pull Request.

### 12. Final testing and persistence verification

After merging the integration branches into `develop`, I used ChatGPT as a
support tool during the final verification process.

I checked the Git history, synchronized my local `develop` branch, compiled
the project, and tested the ten required console operations.

I also verified that products, customers, sales, and the updated stock were
still available after restarting the application.

## Conclusion

ChatGPT was used as a support tool throughout the project, mainly for
clarifying concepts, resolving development doubts, reviewing code,
understanding Git and Maven, and supporting the integration and testing
process.

The final implementation decisions were made based on the actual project
code and were tested after the corresponding changes.