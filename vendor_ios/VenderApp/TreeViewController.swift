//
//  TreeViewController.swift
//  RATreeViewExamples
//
//  Created by Rafal Augustyniak on 21/11/15.
//  Copyright © 2015 com.Augustyniak. All rights reserved.
//


import UIKit
import RATreeView

class TreeViewController: ced_VendorBaseClass, RATreeViewDelegate, RATreeViewDataSource {

    var treeView : RATreeView!
    var data : [DataObject] = []

    convenience init() {
        self.init(nibName : nil, bundle: nil)
    }

    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        //data = TreeViewController.commonInit()
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
    }

    required init?(coder aDecoder: NSCoder) {
        //data = TreeViewController.commonInit()
        super.init(coder: aDecoder)
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white

        let id=UserDefaults.standard.value(forKey: "SUBVENDORID") as! String
        
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
         let vendorId = userData["vendorId"] as! String
         let hashKey    = userData["hashKey"] as! String
         var postString = "vendor_id="+vendorId+"&hashkey="+hashKey
         postString+="&sub_id="+id
         self.sendRequest(url: "vsubaccountapi/customer/view", parameters: postString)
        title = "Things"
        //setupTreeView()
    }

    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        
        if let data = data {
            
            
            
            guard let jsonData = try? JSON(data: data) else {return}
            var dataSource=[DataObject]()
            
            if jsonData["data"]["status"].stringValue=="true"{
                let json=jsonData["data"]["resource_tree"].array
                for item in json!{
                    for (key,value) in item.dictionary!{
                        
                    }
                }
            }
            
            
            
            /*let jsonData = JSON(data:data)
            //print(jsonData)
            var dataSource=[DataObject]()
            let json=jsonData[0]["category_info"].dictionary
            print(json as Any)
            for (key,value) in json!{
                
                let maincatename=key
                let maincateimage=value["icon"].stringValue
                let maincateId=value["id"].stringValue
                var hasChildd="false"
                if value["child"].dictionary != nil{
                    hasChildd="true"
                }
                
                var subDataSource=[DataObject]()
                if hasChildd=="true"{
                    subDataSource=addDataToDataSource(value: value, cateName: maincatename, cateId: maincateId, cateImage: maincateimage)
                    dataSource=subDataSource
                }
            }
            self.data=dataSource
            //table
            setupTreeView()*/
        }
    }
    
    
   func addDataToDataSource(value:JSON, cateName: String, cateId: String, cateImage: String)->[DataObject]{
        
            
        var cellDataSource=[DataObject]()
        let level4=value["child"].dictionary
        for (key,value) in level4!{
            
            let cateName=key
            let cateImage=value["icon"].stringValue
            let cateId=value["id"].stringValue
            var childFlag="false"
            if value["child"].dictionary != nil{
                childFlag="true"
            }
            
            if childFlag=="true"{
                
                let dataSource=addDataToDataSource(value: value, cateName: cateName, cateId: cateId, cateImage: cateImage)
                
                let data=DataObject(name: cateName, children: dataSource ,id: cateId,image:cateImage)
                cellDataSource.append(data)
            }
            else
            {
                let data=DataObject(name: cateName,id: cateId,image:cateImage)
                cellDataSource.append(data)
            }
            
        }
        return cellDataSource
        
    }
    
    @objc func setupTreeView() -> Void {
        let label=UILabel(frame: CGRect(x: 0, y: 0, width: self.view.frame.width, height: 40))
        label.text="Shop By Categories"
        label.textAlignment = .center
        label.textColor=UIColor.white
        label.setThemeColor()
        self.view.addSubview(label)
        treeView = RATreeView(frame: CGRect(x: 0, y: 45, width: self.view.frame.width, height: self.view.frame.height))
        treeView.register(UINib(nibName: String(describing: TreeTableViewCell.self), bundle: nil), forCellReuseIdentifier: String(describing: TreeTableViewCell.self))
        treeView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        treeView.delegate = self;
        treeView.dataSource = self;
        treeView.treeFooterView = UIView()
        treeView.backgroundColor = .clear
        view.addSubview(treeView)
        //treeView.expandRow(forItem: data[0])
    }

    

    

    //MARK: RATreeView data source

    @objc func treeView(_ treeView: RATreeView, numberOfChildrenOfItem item: Any?) -> Int {
        if let item = item as? DataObject {
            return item.children.count
        } else {
            return self.data.count
        }
    }

    var checker:DataObject?=nil
    @objc func treeView(_ treeView: RATreeView, child index: Int, ofItem item: Any?) -> Any {
        print("^^^^^^")
        if let item = item as? DataObject {
            return item.children[index]
        } else {
            
            return data[index] as AnyObject
            
        }
    }

    @objc func treeView(_ treeView: RATreeView, cellForItem item: Any?) -> UITableViewCell {
        print("CELLCREATED")
        let cell = treeView.dequeueReusableCell(withIdentifier: String(describing: TreeTableViewCell.self)) as! TreeTableViewCell
        let item = item as! DataObject

        print(item)
        let level = treeView.levelForCell(forItem: item)
        let detailsText = "Number of children \(item.children.count)"
        cell.selectionStyle = .none
        
        cell.setup(withTitle: item.name, detailsText: detailsText, level: level, additionalButtonHidden: false,img: item.image)
        
        //treeView.expandRow(forItem: <#T##Any?#>, expandChildren: <#T##Bool#>, with: <#T##RATreeViewRowAnimation#>)
        /*cell.additionButtonActionBlock = { [weak treeView] cell in
            guard let treeView = treeView else {
                return;
            }
            let item = treeView.item(for: cell) as! DataObject
            let newItem = DataObject(name: "Added value", )
            item.addChild(newItem)
            treeView.insertItems(at: IndexSet(integer: item.children.count-1), inParent: item, with: RATreeViewRowAnimationNone);
            treeView.reloadRows(forItems: [item], with: RATreeViewRowAnimationNone)
        }*/
        return cell
    }

    //MARK: RATreeView delegate

    @objc func treeView(_ treeView: RATreeView, commit editingStyle: UITableViewCell.EditingStyle, forRowForItem item: Any) {
        
        print("CELL")
        
        guard editingStyle == .delete else { return; }
        let item = item as! DataObject
        let parent = treeView.parent(forItem: item) as? DataObject

        let index: Int
        if let parent = parent {
            index = parent.children.index(where: { dataObject in
                return dataObject === item
            })!
            parent.removeChild(item)

        } else {
            index = self.data.index(where: { dataObject in
                return dataObject === item;
            })!
            self.data.remove(at: index)
        }
         
        self.treeView.deleteItems(at: IndexSet(integer: index), inParent: parent, with: RATreeViewRowAnimationRight)
        if let parent = parent {
            self.treeView.reloadRows(forItems: [parent], with: RATreeViewRowAnimationNone)
        }
    }
    @objc func treeView(_ treeView: RATreeView, didSelectRowForItem item: Any) {
       
        if let data=item as? DataObject{
            if data.children.count == 0{
                
                let selectedId=data.id
                /*if let viewController =  UIStoryboard(name: "categorylayouts", bundle: nil).instantiateViewController(withIdentifier: "cedMageDefaultCollection") as?  cedMageDefaultCollection {
                    viewController.selectedCategory = selectedId
                    self.navigationController?.pushViewController(viewController, animated: true)
                }*/
                
            }
        }
    }
}


private extension TreeViewController {

    static func commonInit() -> [DataObject] {
        return []
    }

}

