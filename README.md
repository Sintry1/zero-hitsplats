# Zero Hitsplats

Shows a blue hitsplat and a scrolling `0` when a melee or ranged attack gives no combat XP. It gives you a visual cue for misses, similar to the XP drops you get when an attack lands.

Use it alongside Customizable XP Drops to keep your usual XP drops on successful hits.

## Setup

- Hold **Alt** to drag the overlay. Release over a RuneLite anchor region to snap it into place.
- Turn on **Preview** to line it up with your XP drops, then turn it off when you're done.
- Adjust **Hitsplat icon size** and **Font size** separately. The text uses the regular RuneScape font, with a default size of 16.
- Change the scroll speed, distance and fade to suit your layout.
- Enable **Attach to player** if you'd rather have the drops follow your character.

Position and appearance settings are separate from Customizable XP Drops.

## How it works

The plugin watches melee and ranged attack animations, projectiles and XP changes. It covers bows, crossbows, blowpipes, thrown weapons, chinchompas, ballistas, atlatls and tonalztics. Salamanders are included in melee and ranged mode. Magic casts and powered-staff attacks are excluded.

Detection isn't perfect. Unusual attacks, delayed XP and targets that award no XP can produce missing or incorrect zeros. Multi-hit attacks are treated as one attack rather than separate hitsplats.

## Build and run

Requires JDK 17. On Windows:

```powershell
.\gradlew.bat test jar
.\gradlew.bat run
```

On macOS or Linux, use `./gradlew` instead. The run command opens a development RuneLite client; enable **Zero Hitsplats** in its plugin list. The plugin isn't on the Plugin Hub yet.

## Credits

Attack animation data comes from [Attack Timer Metronome](https://github.com/ngraves95/attacktimer). Its license is included in [LICENSE](LICENSE). The scrolling display is inspired by [Customizable XP Drops](https://github.com/l2-/template-plugin).
