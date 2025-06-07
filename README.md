- 👋 Hi, I’m @bounce18
- 👀 I’m interested in ...
- 🌱 I’m currently learning ...
- 💞️ I’m looking to collaborate on ...
- 📫 How to reach me ...

<!---
bounce18/bounce18 is a ✨ special ✨ repository because its `README.md` (this file) appears on your GitHub profile.
You can click the Preview link to take a look at your changes.
--->

## GE Flipping Helper Plugin

This repository now includes a RuneLite plugin located in `ge-flipping-plugin/`. The plugin displays your coin stack and suggests Grand Exchange items to flip using price data from the OSRS Wiki API. It now also factors in recent trade volumes and ranks the top items you can afford by their total potential profit.

The UI shows a table with icons, quantities to buy, prices to use, and the profit for each suggestion.

### Build Instructions

Install Java 11 and Gradle. Then run:

```bash
cd ge-flipping-plugin
./gradlew build
```

The resulting JAR can be installed into RuneLite.
