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

class ced_vendorLatestProducts: UITableViewCell,UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout {
    
    @IBOutlet weak var colorViewHeight: NSLayoutConstraint!
    @IBOutlet weak var headerView: UIView!
    @IBOutlet weak var coloredView: UIView!
    @IBOutlet weak var seeCounrtyList: UIButton!
    @IBOutlet weak var pages: UIPageControl!
    @IBOutlet weak var productStatuse: UILabel!
    @IBOutlet weak var ImageView: UIImageView!
    @IBOutlet weak var productCollectionView: UICollectionView!
    @IBOutlet weak var noDataLabel: UILabel!
    var parent = UIViewController()
    var latestOrders = [[String:String]]()
    var mapData = [[String:String]]()
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        seeCounrtyList.backgroundColor = color
        ImageView.backgroundColor = color
        headerView.backgroundColor = color
        productStatuse.backgroundColor = color
        productCollectionView.delegate = self
        productCollectionView.dataSource = self
        colorViewHeight.constant = productCollectionView.frame.height/2
        productCollectionView.layer.borderWidth = 0.2
        productCollectionView.layer.borderColor = UIColor.black.cgColor
        seeCounrtyList.setTitle("See State Wise Orders Here".localized, for: .normal)
        seeCounrtyList.addTarget(self, action: #selector(ced_vendorLatestProducts.seecountryList(_:)), for: UIControl.Event.touchUpInside)
        
    }

    @objc func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    @objc func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return latestOrders.count
    }
    @objc func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = productCollectionView.dequeueReusableCell(withReuseIdentifier: "latestorder", for: indexPath) as! ced_latestProductsCell
        cell.orderIDTitle.text = "Order Id".localized
        cell.netEarnedTitle.text="Net Earned".localized
        cell.statusTitle.text = "Order Status".localized
        cell.billingTitle.text = ""//"Billing Name".localized
        cell.purchasedTitle.text = "Purchased On".localized
        cell.orderId.text = "#"+latestOrders[indexPath.row]["order_id"]!
        cell.netEarned.text = latestOrders[indexPath.row]["net_earned"]
        cell.billingName.text = ""//latestOrders[indexPath.row]["billing_name"]
        cell.orderStatus.text = "#"+latestOrders[indexPath.row]["order_status"]!.uppercased()
        cell.orderStatus.numberOfLines = 0
        if(latestOrders[indexPath.row]["order_status"] == "payment_accepted"){
            coloredView.backgroundColor = UIColor.blue
            cell.orderStatus.textColor = UIColor.blue
        }else if(latestOrders[indexPath.row]["order_status"] == "complete"){
            coloredView.backgroundColor = UIColor.green
             cell.orderStatus.textColor = UIColor.green
        }
        else if(latestOrders[indexPath.row]["order_status"] == "pending"){
            coloredView.backgroundColor = UIColor.orange
            cell.orderStatus.textColor = UIColor.orange
        }
        cell.purchasedOn.text = latestOrders[indexPath.row]["purchase_on"]
        return cell
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let item = latestOrders[indexPath.row]
        let storybordOrder = UIStoryboard(name: "OrderAddon", bundle: nil)
        let viewcontrol = storybordOrder.instantiateViewController(withIdentifier: "manageOrders") as! ced_manageProduct
        
        viewcontrol.orderId = item["order_id"]!
        parent.navigationController?.pushViewController(viewcontrol, animated: true)
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return productCollectionView.bounds.size
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @objc func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let  pageWidth = self.productCollectionView.frame.size.width;
        self.pages.currentPage = Int(self.productCollectionView.contentOffset.x / pageWidth)
    }
    @objc func seecountryList(_ sender:UIButton){
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        let viewcontrol = storyBoard.instantiateViewController(withIdentifier: "gmsMap") as! ced_ordersListMap
        viewcontrol.mapData = mapData
        self.parent.navigationController?.pushViewController(viewcontrol, animated: true)
    }

}
