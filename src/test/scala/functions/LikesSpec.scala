/*
 * Copyright (C) 2018 Simer Plaha (@simerplaha)
 *
 * This file is a part of SwayDB.
 *
 * SwayDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * SwayDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with SwayDB. If not, see <https://www.gnu.org/licenses/>.
 */

package functions

import base.TestBase
import swaydb._ //import API
import swaydb.serializers.Default._ //import default serializers

class LikesSpec extends TestBase {

  "increment likes count" in {

    val likesMap = memory.Map[String, Int, String]().get //create likes database map.

    likesMap.put(key = "SwayDB", value = 0) //initial entry with 0 likes.

    //function that increments likes by 1
    //in SQL this would be "UPDATE LIKES_TABLE SET LIKES = LIKES + 1"
    def incrementLikes(currentLikes: Int) = Apply.Update(currentLikes + 1)

    //register the above likes function
    val likesFunctionId =
      likesMap.registerFunction(
        functionId = "increment likes counts",
        function = incrementLikes(_)
      )

    //concurrently apply 100 likes to key SwayDB using the above registered function's functionId.
    (1 to 100).par foreach {
      _ =>
        likesMap.applyFunction("SwayDB", likesFunctionId).get
    }

    //total likes == 100
    likesMap.get("SwayDB").get should contain(100)
  }
}