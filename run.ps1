$ErrorActionPreference = 'Stop'

& "$PSScriptRoot/build.ps1"
java -cp 'lib/*;build/classes' main.Main
if ($LASTEXITCODE -ne 0) {
    throw "The application exited with an error."
}
