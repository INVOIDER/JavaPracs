// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;
import "WhiteList.sol"; // Импортируем WhiteList
contract MyShop {

    address public owner;
    mapping (address => uint) public payments;
    Whitelist public whitelist;

    modifier onlyWhitelisted() {
        require(whitelist.isUserInWhitelist(msg.sender), "Access denied: user not in whitelist");
        _;
    }
    modifier onlyOwner(){
        require(msg.sender == owner, "Only the owner can call this function");
        _; 
    }
    constructor(address _whitelistAddress) {
        owner = msg.sender;
        whitelist = Whitelist(_whitelistAddress);
    }

    function payForItem() payable external onlyWhitelisted{
        payments[msg.sender] = msg.value;
    }

    function withdrawAll() external onlyOwner {
        address payable _to = payable(owner);
        address _thisContract = address(this);
        _to.transfer(_thisContract.balance);
    }
}
