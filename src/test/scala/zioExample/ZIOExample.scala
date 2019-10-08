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

package zioExample

import base.TestBase
import base.UserTable.UserValues.ExpiredUser
import base.UserTable.{UserFunctions, UserKeys, UserValues}
import zio.{DefaultRuntime, Task}
import org.scalatest.OptionValues._

class ZIOExample extends TestBase {

  "ZIO example with functions" in {
    implicit val runtime = new DefaultRuntime {}
    import swaydb.zio.Tag._ //import zio tag to support Task.

    val map = swaydb.memory.Map[UserKeys, UserValues, UserFunctions, Task]().get //Create a memory database

    //functions should always be registered on database startup.
    map.registerFunction(UserFunctions.ExpireUserFunction)

    val userName = UserKeys.UserName("iron_man")
    val activeUser = UserValues.ActiveUser(name = "Tony Stark", email = "tony@stark.com", lastLogin = System.nanoTime())

    //write the above user as active
    map.put(key = userName, value = activeUser).awaitTask

    //get the user from the database it will result active.
    map.get(userName).awaitTask.value shouldBe activeUser

    //expire user using the registered ExpireFunction
    map.applyFunction(key = userName, function = UserFunctions.ExpireUserFunction).awaitTask

    //the function expires the user "iron_man" - blame Thanos!
    map.get(userName).awaitTask.value shouldBe ExpiredUser
  }
}