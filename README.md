# Kag

Default sidecar frontend for [Kitsune](https://github.com/valerauko/kitsune).

## Overview

FIXME: Write a paragraph about the library/project and highlight its goals.

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.

## License

Copyright (C) 2018 @[valerauko](https://github.com/valerauko)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License along with this program. If not, see https://www.gnu.org/licenses/.

### Additional permission under GNU AGPL section 7:

If you modify this Program, or any covered work, by linking or combining it with its dependencies  (or a modified version thereof) as listed in [project.clj](project.clj), containing parts covered by the terms of the dependencies' respective licenses, the licensors of this Program grant you additional permission to convey the resulting work.

Corresponding Source for a non-source form of such a combination shall include the source code for the parts of the dependencies used as well as that of the covered work.
