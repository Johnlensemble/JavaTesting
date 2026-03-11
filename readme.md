Before starting

Install Java (https://www.java.com/en/download/manual.jsp java 8 for example)

Maven provides the spring-boot:run goal.
Check if installed:
    mvn -version
If not installed:
macOS
    brew install maven
Ubuntu/Debian
    sudo apt install maven
Windows
    Download Maven ZIP from the Apache site and add bin to PATH.



Quick map of the most impactful changes (OpenJDK-only sources)
	•	Modules (JDK 9): Java Platform Module System (JPMS) for strong encapsulation and custom runtime images. [openjdk.org]
	•	JShell (JDK 9): official Java REPL for rapid experiments. [openjdk.org]
	•	var (JDK 10): local-variable type inference. [openjdk.org]
	•	HTTP Client (JDK 11): standard java.net.http with HTTP/2 and async APIs. [openjdk.org]
	•	Switch expressions (JDK 14): concise, expression form of switch. [openjdk.org]
	•	Text blocks (JDK 15): multi-line string literals. [openjdk.org]
	•	Records (JDK 16): concise, immutable data carriers. [openjdk.org]
	•	Pattern matching for instanceof (JDK 16): test + bind in one step. [openjdk.org]
	•	Sealed classes (JDK 17): restrict who can extend/implement. [openjdk.org]
	•	Pattern matching for switch (JDK 21) and Record patterns (JDK 21): data‑centric control flow and deconstruction. [openjdk.org], [openjdk.org]
	•	Virtual Threads (JDK 21): lightweight threads for massive concurrency (Project Loom). [openjdk.org]
	•	Sequenced Collections (JDK 21): uniform first/last/reversed APIs. [openjdk.org]
	•	UTF‑8 by default (JDK 18): predictable text I/O across platforms. [openjdk.org]

1) Language upgrades you’ll feel immediately
var — local‑variable type inference (JDK 10)
Declutter locals while keeping static typing. [openjdk.org]

var map = new java.util.HashMap<String, Integer>(); // infers HashMap<String,Integer>
for (var e : map.entrySet()) {
    System.out.println(e.getKey() + " -> " + e.getValue());
}

Switch expressions (JDK 14)
Use switch as an expression; arrow labels, yield when needed. [openjdk.org]

int len = switch (day) {
  case MONDAY, FRIDAY, SUNDAY -> 6;
  case TUESDAY                -> 7;
  case THURSDAY, SATURDAY     -> 8;
  case WEDNESDAY              -> 9;
};

Text blocks """ (JDK 15)
Multi‑line strings for JSON/SQL/HTML, fewer escapes. [openjdk.org]

String json = """
  { "user": "ada", "role": "admin" }
  """;

Records (JDK 16)
One‑line value types; canonical ctor, accessors, equals/hashCode/toString auto‑generated. [openjdk.org]
public record User(String id, String email) {}
Pattern matching for instanceof (JDK 16)
Eliminate the “instanceof + cast” boilerplate. [openjdk.org]

if (obj instanceof String s) {
    System.out.println(s.toUpperCase());
}

Sealed classes & interfaces (JDK 17)
Close a hierarchy to a known set of subtypes—great with pattern matching. [openjdk.org]

sealed interface Shape permits Circle, Rectangle {}
record Circle(double r) implements Shape {}
record Rectangle(int w, int h) implements Shape {}

Pattern matching for switch (JDK 21)
Switch on any reference type; use type patterns, guards, and handle null. [openjdk.org]

static String describe(Object o) {
  return switch (o) {
    case Integer i when i > 0 -> "positive " + i;
    case String  s            -> "string:" + s.toUpperCase();
    case null                 -> "null";
    default                   -> o.toString();
  };
}

Record patterns (JDK 21)
Deconstruct records inline in instanceof/switch. [openjdk.org]

record Point(int x, int y) {}
int sum(Object o) {
  return (o instanceof Point(int x, int y)) ? x + y : 0;
}


2) Concurrency and performance
Virtual Threads (JDK 21)
Massively increase concurrency with thread‑per‑task style, ideal for I/O‑bound servers. [openjdk.org]

try (var exec = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
  var futures = java.util.stream.IntStream.range(0, 1_000)
      .mapToObj(i -> exec.submit(() -> fetchFromService(i))) // blocking calls are fine
      .toList();
  for (var f : futures) System.out.println(f.get());
}

Generational ZGC (JDK 21)
ZGC adds generations to reduce stalls/overhead while preserving low pauses; enable per your GC choice. [openjdk.org]

3) Standard library & tools
HTTP Client (JDK 11)
Modern java.net.http with HTTP/2, sync/async, and reactive streams; replaces HttpURLConnection. [openjdk.org]

var client = java.net.http.HttpClient.newHttpClient();
var req    = java.net.http.HttpRequest.newBuilder(URI.create("https://example.com")).build();
var res    = client.send(req, java.net.http.HttpResponse.BodyHandlers.ofString());
System.out.println(res.statusCode());

Sequenced Collections (JDK 21)
New interfaces SequencedCollection/SequencedSet/SequencedMap give first/last and reversed views uniformly. [openjdk.org]

var list = new java.util.ArrayList<>(java.util.List.of("a","b","c"));
System.out.println(list.getFirst());  // "a"
System.out.println(list.getLast());   // "c"
System.out.println(list.reversed());  // view: ["c","b","a"]

JShell (JDK 9)
Try APIs and language snippets interactively: jshell. [openjdk.org]
UTF‑8 by default (JDK 18)
Default charset standardized to UTF‑8 across platforms, making text I/O behavior consistent. [openjdk.org]

4) Platform architecture change (JDK 9)
Modules (JPMS)
Adopt modules for strong encapsulation; use module-info.java and tools like jlink for trimmed runtime images. [openjdk.org]

// module-info.java
module com.example.app {
  requires java.net.http;
  exports com.example.api;
}


5) “Before → After” mini refactor (ties features together)
Before (Java 8‑style):

// Polymorphic logic + boilerplate
double area(Object s) {
  if (s instanceof Circle) {
    Circle c = (Circle) s;
    return Math.PI * c.r() * c.r();
  } else if (s instanceof Rectangle) {
    Rectangle r = (Rectangle) s;
    return r.w() * r.h();
  }
  throw new IllegalArgumentException();
}
``

After (Java 21): records + sealed + pattern matching for switch + record patterns. [openjdk.org], [openjdk.org], [openjdk.org]

sealed interface Shape permits Circle, Rectangle {}
record Circle(double r) implements Shape {}
record Rectangle(int w, int h) implements Shape {}

double area(Shape s) {
  return switch (s) {
    case Circle(double r)      -> Math.PI * r * r;
    case Rectangle(int w, int h)-> w * h;
  };
}


