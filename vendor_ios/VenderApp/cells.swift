/**
 * CedCommerce
 *
 * NOTICE OF LICENSE
 *
 * This source file is subject to the End User License Agreement (EULA)
 * that is bundled with this package in the file LICENSE.txt.
 * It is also available through the world-wide-web at this URL:
 * http://cedcommerce.com/license-agreement.txt
 *
 * @category  Ced
 * @package   MageNative
 * @author    CedCommerce Core Team <connect@cedcommerce.com >
 * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 * @license      http://cedcommerce.com/license-agreement.txt
 */

import Foundation

class expandingCells {
     fileprivate (set) var totalitems = [cell]()
 
    class cell {
        var hidden:Bool;
        var textValue: String;
        var imageUrl:String;
        var categoryId: String;
        var hasChild: String;
        init(_ hidden: Bool = true, value: String,imageUrl:String,cateId:String,hasChild:String) {
            self.hidden = hidden
            self.textValue = value
            self.categoryId = cateId
            self.hasChild = hasChild
            self.imageUrl = imageUrl
        }
    }
    
    
    class headerValues : cell{
        init (value: String,image:String,cate:String,haschild:String) {
            super.init(false, value: value,imageUrl: image,cateId:cate ,hasChild:haschild)
        }
    }
    
    func append(_ item: cell) {
        self.totalitems.append(item)
    }
    
    @objc func removeAll() {
        self.totalitems.removeAll()
    }
    
    @objc func expand(_ headerIndex: Int) {
        self.toogleVisible(headerIndex, isHidden: false)
    }
    
    @objc func collapse(_ headerIndex: Int) {
        self.toogleVisible(headerIndex, isHidden: true)
    }
    
    private func toogleVisible(_ headerIndex: Int, isHidden: Bool) {
        var headerIndex = headerIndex
        headerIndex += 1
        while headerIndex < self.totalitems.count && !(self.totalitems[headerIndex] is headerValues) {
            self.totalitems[headerIndex].hidden = isHidden
            
            headerIndex += 1
        }
    }
}
