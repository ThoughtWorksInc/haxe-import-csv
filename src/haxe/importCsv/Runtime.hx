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

package importCsv;

import haxe.macro.Type;
import haxe.macro.Expr;
import haxe.macro.*;

@:dox(hide)
class Runtime
{
  macro public static function parseCell(cellContent:ExprOf<String>):Expr return
  {
    function parseByType(expectedType:Type):Expr return
    {
      function parseDefaultCell(cellContent:ExprOf<String>):Expr return
      {
        switch (cellContent)
        {
          case { pos: pos, expr: EConst(CString(code)) }:
          {
            Context.parse(code + "\n", pos);
          }
          case { pos: pos } :
          {
            Context.error("Expected \"", pos);
          }
        }
      }

      function parseBaseType(baseType:BaseType):Expr return
      {
        for (entry in baseType.meta.get())
        {
          switch (entry)
          {
            case { name: ":parseCellFunction", params: [ functionExpr ] } :
            {
              return macro $functionExpr($cellContent);
            }
            default:
            {
              continue;
            }
          }
        }
        var followable = switch (expectedType)
        {
          case TMono(_): true;
          case TLazy(_): true;
          case TType(_, _): true;
          case TInst(_.get() => { kind: KGenericBuild }, _): true;
          default: false;
        }
        if (followable)
        {
          parseByType(Context.follow(expectedType, true));
        }
        else
        {
          parseDefaultCell(cellContent);
        }
      }
      switch (expectedType)
      {
        case TInst(t, _): parseBaseType(t.get());
        case TAbstract(t, _): parseBaseType(t.get());
        case TType(t, _): parseBaseType(t.get());
        default: parseDefaultCell(cellContent);
      }
    }
    parseByType(Context.getExpectedType());
  }
}
