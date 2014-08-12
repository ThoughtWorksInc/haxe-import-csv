/*
 * haxe-import-csv
 * Copyright 2014 深圳岂凡网络有限公司 (Shenzhen QiFun Network Corp., LTD)
 *
 * Author: 杨博 (Yang Bo) <pop.atry@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qifun.qforce.importCsv;
import haxe.macro.Expr;
import haxe.macro.*;

@:nativeGen
@:autoBuild(com.qifun.qforce.importCsv.ImportedRow.ImportedRowBuilder.build())
class ImportedRow
{

  inline function new() {}

  var y(get, never):Bool;

  @:protected
  inline function get_y() return true;

  var n(get, never):Bool;

  @:protected
  inline function get_n() return false;


}

#if macro
class ImportedRowBuilder
{

  public static function build():Array<Field> return
  {
    // TODO: 创建toString、equals和hashCode函数
    Context.getBuildFields();
  }

}
#end

