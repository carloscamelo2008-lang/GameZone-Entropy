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

During the development of the GameZone Unicesar project, I used ChatGPT as a
support tool to clarify doubts, understand development tools, review my own
code, interpret compiler errors, understand the work developed by the other
team members, and integrate the different modules of the project.

The AI was used mainly when I encountered situations that I did not fully
understand. I used the explanations and suggestions as support, but I
verified them against the actual project code and adapted them according to
the implementation of the team.

## AI Usage Records

### 1. Understanding the workshop requirements

At the beginning of the project, I had several doubts about the requirements
of the workshop and how the different parts of the project had to be
organized.

I used ChatGPT to help me understand the required project structure, the
four application layers, the Git workflow, the required operations of the
system, and the responsibilities associated with my role as Technical
Leader.

This helped me organize the development process and understand how the
different modules of the project would later be integrated.

---

### 2. Understanding Git and Git Flow

One of the areas where I needed more help was Git, especially the use of
branches, Pull Requests, and the relationship between `main`, `develop`, and
the feature branches.

I used ChatGPT to understand how to create feature branches, how to switch
between branches, how to push changes to GitHub, how to create Pull Requests,
and how to follow the workflow required by the workshop.

I also used ChatGPT when reviewing the Pull Requests made by the other team
members. The explanations helped me understand what each change did and
allowed me to review the code before approving and merging it.

Through this process I became more familiar with the Git workflow used during
the project.

---

### 3. Reviewing the Product module

The first developer was responsible for the product module. After that work
was completed, I reviewed the classes related to products, repositories, and
services.

I used ChatGPT to help me understand parts of the implementation and to
identify possible problems or improvements in the code.

I compared the suggestions with the actual code in the repository and used
my own judgment to determine which points were relevant.

This review helped me better understand the separation between the model,
persistence, and service layers.

---

### 4. Reviewing the Person module

The second developer was responsible for the person module, including
customers and sellers.

I used ChatGPT to help me review the classes related to `Person`, `Customer`,
`Seller`, `PersonRepository`, and `PersonService`.

This was useful for understanding how the information of customers and
sellers was stored and how it would later be used by the sales module.

I also used the explanations to better understand the role of the service
layer when working with persistent information.

---

### 5. Understanding JavaDoc

During the implementation, I had doubts about the JavaDoc requirements of
the workshop and how the documentation should be written.

I used ChatGPT to understand where the JavaDoc should be placed and to help
me write documentation for classes and methods in English.

This was especially useful when adding the methods of the user interface,
because I needed to keep the source-code documentation in English while the
messages shown to the user could be written in Spanish.

---

### 6. Learning to use Maven

I was not very familiar with Maven and needed help understanding how it was
being used in the project.

I used ChatGPT to help me understand how to compile and rebuild the project,
how Maven related to the Java project, and how to interpret the results of
the build process.

---

### 7. Integrating the application services into the UI

My main responsibility as Technical Leader included integrating the different
services through the console interface.

I used ChatGPT to help me organize the `UI` class and understand how the user
interface should communicate with the service layer.

The objective was to make the UI use the services without accessing the
persistence layer directly.

This helped me understand better the responsibility of each layer and how
the UI acts as the point of interaction with the user.

---

### 8. Implementing product operations in the UI

After the Product module had already been developed, I needed to connect it
to the console interface.

I used ChatGPT to help me structure the UI operations for:

- registering a video game,
- registering a console,
- listing products.

During this process I also encountered compilation errors because some
method signatures in the actual project were different from the parameters
I initially expected.

I checked the real `ProductService` implementation and adapted the UI to its
actual methods.

This helped me understand the importance of checking the real signatures of
the classes in the project instead of assuming that a method has a specific
parameter list.

---

### 9. Resolving compiler errors

While developing the UI, I encountered several compiler errors.

For example, IntelliJ reported errors related to incorrect numbers of
arguments and methods that could not be resolved.

I used ChatGPT to interpret the compiler messages and understand what was
causing them.

After receiving the explanation, I checked the corresponding classes in the
project and corrected the UI according to the actual implementation.

This helped me learn how to use compiler errors as a guide to locate the real
problem instead of trying to correct every error individually.

---

### 10. Implementing customer and seller operations in the UI

The UI needed to provide the operations related to customers and sellers.

I used ChatGPT to help me organize the console interaction for registering
customers, listing customers, and listing sellers.

The existing business logic was already contained in the corresponding
services, so my task was to connect those operations to the UI and display
the results appropriately.

I tested the operations directly from the console to verify that the
information was being displayed correctly.

---

### 11. Implementing sale registration in the UI

One of the most important parts of my work was connecting the existing
`SaleService` to the console interface.

I used ChatGPT to understand how the UI should collect the customer ID,
seller ID, and the IDs of the products included in the sale.

I also used the explanations to understand how the existing service handled
the validation of the customer, seller, products, stock, total calculation,
and persistence of the sale.

After integrating the functionality, I tested the registration of sales and
verified that the sale was stored and that the product stock was updated.

I also tested the case of adding the same product more than once to represent
multiple units of the same product.

---

### 12. Implementing sales history operations

After implementing sale registration, I connected the remaining sales
queries to the UI.

These operations were:

- displaying the complete sales history,
- displaying the purchase history of a customer,
- displaying the sales history of a seller.

I used ChatGPT to help me structure the console output and understand which
existing `SaleService` method corresponded to each operation.

The actual filtering logic remained in the service layer, while the UI was
responsible for requesting the corresponding ID and displaying the results.

I tested these operations using sales previously created in the application.

---

### 13. Clarifying the language used in the application

At one point I became confused about whether the requirement for English
applied to everything in the application.

I used ChatGPT to clarify the difference between the language of the source
code and the language of the user interface.

After reviewing the requirement, I kept the class, attribute, and method names
in English, as well as the JavaDoc, while using Spanish for the messages
displayed to the user in the console.

This helped me maintain consistency between the project requirements and the
user interface.

---

### 14. Handling invalid menu options

During testing I noticed that the main menu did not explicitly handle an
invalid option.

I used ChatGPT to understand how the `default` case of a `switch` statement
could be used for this situation.

I then added a default case that displays an error message when the user
enters an option that is not available.

I tested the behavior by entering an invalid menu option and confirming that
the program continued running correctly.

---

### 15. Managing runtime data files with Git

Before preparing the branch for the Pull Request, I used `git status` and
noticed that several files generated by the application inside the `data`
directory were being detected as untracked files.

I used ChatGPT to understand why Git was detecting those files and how the
`.gitignore` file should be configured.

I then updated `.gitignore` so that the runtime CSV and DAT files generated
by the application would not be accidentally committed.

I also used a `.gitkeep` file so that the `data` directory would remain
represented in the repository.

This helped me understand better how `.gitignore` works and how generated
files can be separated from the source code of the project.

---

### 16. Final project verification

Before preparing the Pull Request, I used ChatGPT to help me review the
project structure, Git status, commit history, and build process.

I verified that the working tree was clean, that the feature branch contained
the expected commits, and that the project could be rebuilt successfully.

I also reviewed the project structure to confirm the presence of the required
source-code packages, documentation files, and data directory.

This final verification helped me prepare the feature branch before merging
it into `develop`.

## Conclusion

ChatGPT was used throughout the development process as a support tool for
learning, understanding errors, reviewing code, using Git and Maven, and
integrating the different parts of the project.

The main situations in which I used AI were related to Git and Git Flow,
JavaDoc, Maven, compiler errors, UI integration, sales integration, and
understanding code developed by the other members of the team.

The suggestions provided by the AI were compared with the actual project
implementation and adapted when necessary. I also tested the changes after
making them.

This process helped me strengthen my understanding of Java, object-oriented
programming, layered architecture, Git, Maven, persistence, and the
integration of different modules in a team project.