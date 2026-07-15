$ErrorActionPreference = 'Stop'

if (Test-Path build) {
    Remove-Item -Recurse -Force -LiteralPath build
}

New-Item -ItemType Directory -Path build/classes | Out-Null
$sources = Get-ChildItem -Recurse -File src -Filter *.java |
    Where-Object FullName -NotMatch '\\test\\' |
    ForEach-Object FullName

javac -encoding UTF-8 -cp 'lib/*' -d build/classes $sources
if ($LASTEXITCODE -ne 0) {
    throw "Production source compilation failed."
}
