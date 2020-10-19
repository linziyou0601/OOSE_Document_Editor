# OOSE_Document_Editor
A teamwork of OOSE courses for partial Java implementation of the Lexi Editor from the book Design Patterns by the GoF.

## Key Functionality
- Can write characters on the app and insert images using the menu item.
- Can save and open document.
- Color and background color is changeable.
- Allowing change font style to bold, italic and underlined.

## Design Pattens Implemented
- Composite Pattern has been used to creat a Glyph hierarchy such as Paragraph, Image, and Characters.
- Strategy Pattern provides different formatting algorithms.
- Decorate Pattern has been used to create decorator glyph like Bold, Italic, FontColor, etc.
- Simple Factory was used to create the different kind of Glyph.
- Abstract Factory was used to create different style of widgets which are Linux and Windows.
- Bridge Pattern for implementing frame's design, operating and font style in different kind of Operating System (Simulating Linux on Windows).
- Iterator Pattern has been used to design our own storage structure and iterator.
- Visitor Pattern for visiting each Glyphs inside the Root Glyph to format the document and count the numbers of paragraphs and characters.

## Technology Stack
Java, Swing

## Badge
![GitHub last commit](https://img.shields.io/github/last-commit/linziyou0601/OOSE_Document_Editor?style=for-the-badge) ![](https://img.shields.io/badge/language-java-blue.svg?style=for-the-badge) ![](https://img.shields.io/badge/author-linziyou0601-red.svg?style=for-the-badge)