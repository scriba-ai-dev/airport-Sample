#!/usr/bin/env bun
// Deterministic flat export of the Micro Focus ISAM file `airports.dat`.
// Record layout (from airports.str), fixed 104 bytes, key = f-code:
//   f-code    PIC X(4)    1:4
//   f-name    PIC X(30)   5:30
//   f-city    PIC X(30)   35:30
//   f-country PIC X(20)   65:20
//   f-geo     20 bytes    85:20
//     f-lat-sign  X      85:1   f-lat-degs 9(3) 86:3  f-lat-mins 9(6) 89:6
//     f-long-sign X      95:1   f-long-degs 9(3) 96:3 f-long-mins 9(6) 99:6
// Emits: airports.seq (104-byte fixed sequential, 1:1 to f-rec) + airports.csv (review).

const REC = 104
const src = await Bun.file("src/airport/airports.dat").bytes()

const ascii = (b: Uint8Array) => new TextDecoder("latin1").decode(b)
const isCode = (s: string) => /^[A-Z0-9 ]{4}$/.test(s)
const isGeo = (s: string) => /^[+-]\d{9}[+-]\d{9}$/.test(s)
const printable = (s: string) => /^[\x20-\x7E]+$/.test(s)

const recs = new Map<string, Uint8Array>()
for (let i = 0; i + REC <= src.length; i++) {
  const slice = src.subarray(i, i + REC)
  const code = ascii(slice.subarray(0, 4))
  const name = ascii(slice.subarray(4, 34))
  const geo = ascii(slice.subarray(84, 104))
  if (!isCode(code) || code.trim() === "" || !isGeo(geo) || !printable(name)) continue
  const key = code.trim()
  if (!recs.has(key)) recs.set(key, Uint8Array.from(slice))
  i += REC - 1 // a valid record consumes its slot; skip ahead
}

const keys = [...recs.keys()].sort()
const seq = new Uint8Array(keys.length * REC)
keys.forEach((k, idx) => seq.set(recs.get(k)!, idx * REC))
await Bun.write("src/airport/airports.seq", seq)

const csv = ["code,name,city,country,lat_sign,lat_degs,lat_mins,long_sign,long_degs,long_mins"]
for (const k of keys) {
  const r = ascii(recs.get(k)!)
  csv.push(
    [
      r.slice(0, 4).trim(),
      r.slice(4, 34).trim(),
      r.slice(34, 64).trim(),
      r.slice(64, 84).trim(),
      r.slice(84, 85),
      r.slice(85, 88),
      r.slice(88, 94),
      r.slice(94, 95),
      r.slice(95, 98),
      r.slice(98, 104),
    ]
      .map((c) => (c.includes(",") ? `"${c}"` : c))
      .join(","),
  )
}
await Bun.write("src/airport/airports.csv", csv.join("\n") + "\n")

console.log(`extracted ${keys.length} records -> airports.seq (${seq.length} bytes), airports.csv`)
console.log(`first: ${keys[0]}  last: ${keys[keys.length - 1]}`)
console.log(`sample KEF present: ${recs.has("KEF")}  LHR present: ${recs.has("LHR")}`)
