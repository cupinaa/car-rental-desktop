$ErrorActionPreference = 'Stop'

& "$PSScriptRoot/build.ps1"
New-Item -ItemType Directory -Path build/test-classes | Out-Null
$testSources = Get-ChildItem -Recurse -File src/test -Filter *.java | ForEach-Object FullName
javac -encoding UTF-8 -cp 'lib/*;build/classes' -d build/test-classes $testSources
if ($LASTEXITCODE -ne 0) {
    throw "Test source compilation failed."
}

$testClasses = Get-ChildItem -Recurse -File build/test-classes -Filter '*Test.class' |
    ForEach-Object {
        $_.FullName.Substring((Resolve-Path build/test-classes).Path.Length + 1).
            Replace('\', '.').Replace('.class', '')
    }

java -cp 'lib/*;build/classes;build/test-classes' org.junit.runner.JUnitCore $testClasses
if ($LASTEXITCODE -ne 0) {
    throw "JUnit tests failed."
}
