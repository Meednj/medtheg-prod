# Frontend Motion

Motion is reserved for media, artwork, and state changes that benefit from a
small amount of movement. The frontend uses CSS and the existing motion tokens;
there is no Motion/Framer Motion dependency in the current package.

## Shared motion tokens

`frontend/src/styles/motion.css` defines `--motion-micro`, `--motion-object`,
and `--motion-editorial` durations plus a shared easing curve. The global
`prefers-reduced-motion` rules in `src/index.css` and `src/styles/motion.css`
remove nonessential transitions and stop decorative choreography. State and
playback updates remain immediate.

## Existing surfaces

- The homepage hero dust title uses a bounded canvas particle formation and
  returns to the semantic heading. Reduced-motion users see the heading
  immediately.
- Artwork focus and hover share their treatment; movement is limited to subtle
  crop/registration shifts and does not affect layout dimensions.
- Category navigation opts into React Router view transitions. Product
  artwork uses a stable shared transition name where source and target artwork
  are both present.
- The audio player reflects actual HTML media state. It does not animate a
  fabricated waveform; preview controls remain usable without decorative
  movement.
- Production Desk rows use a short, capped entrance stagger. Cover artwork,
  focus, and button states use the shared object/micro timing. Upload, save,
  route navigation, and preview playback are not delayed by animation.

## Verification

Check the interface with the operating system's reduced-motion preference and
keyboard focus. CSS honors `prefers-reduced-motion`; no claim of an automated
browser motion test is made by the current lint/build checks.
