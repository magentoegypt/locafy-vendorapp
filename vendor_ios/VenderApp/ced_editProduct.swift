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
* @package   MageNative MultiVendor
* @author    CedCommerce Core Team <connect@cedcommerce.com >
* @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
* @license      http://cedcommerce.com/license-agreement.txt
*/
import UIKit

class ced_editProduct: UIViewController,UITableViewDelegate,UITableViewDataSource {

    @IBOutlet weak var editProductTable: UITableView!
    var addimgbool = false
    var number = Int()
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        editProductTable.delegate = self
        editProductTable.dataSource = self
    }

    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 4
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.row == 0){
        let cell = editProductTable.dequeueReusableCell(withIdentifier: "editProduct")!
        return cell
        }else if(indexPath.row == 1){
            let cell = editProductTable.dequeueReusableCell(withIdentifier: "slectwebsite")!
            return cell
        }
        else if(indexPath.row == 2){
            let cell = editProductTable.dequeueReusableCell(withIdentifier: "selectCate")!
            return cell
        }
        else {
            let cell = editProductTable.dequeueReusableCell(withIdentifier: "addImages") as! ced_addImageCell
            cell.addImageButton.addTarget(self, action: #selector(ced_editProduct.addImageView(_:)), for: UIControl.Event.touchUpInside)
            cell.addImageButton.tag = indexPath.row
            
            return cell
        }
    }
    
    @objc func addImageView(_ sender:UIButton){
        addimgbool = true
        let indexpath = IndexPath(row: sender.tag, section: 0)
        let cell = editProductTable.cellForRow(at: indexpath)
        let filterbyView = ced_addImagesProduct();
        filterbyView.tag = 181;
      
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x: 0, y: (cell?.contentView.frame.size.height)! + 2, width: (cell?.contentView.frame.width)! ,  height: 200)
        cell?.contentView.addSubview(filterbyView)
        editProductTable.reloadRows(at: [indexpath], with: UITableView.RowAnimation.none)
         number += 1
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
       
        if(indexPath.row == 0){
        return 1100
        }else if(indexPath.row == 1){
             return 50
        }else if(indexPath.row == 2){
            return 50
        }else {
            if(number > 1){
               
                return CGFloat(number*200)
                
            }
            return 100
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
