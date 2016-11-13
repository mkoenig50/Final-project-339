//Super class for all movies, this will be the baseline.
class Movie
{
  Private boolean available;
  Private double cost = 3.00;
  Private String title;
  Private string rentedTo;
  Movie(String newTitle)
  {
    available = true;
    title = newTitle;
    rentedTo = "";
  }

  Public boolean isAvailable()
  {
    return available;
  }

  Public String rentedBy()
  {
    return rentedTo;
  }
  Public void checkout(String name)
  {
    available = false;
    rentedTo = name;
  }

  Public int rentalCost(int days)
  {
    if(days <= 3)
    {
      return cost * days;
    }
    else
    {
      return (cost * 3) + (cost * 1.5 * (days - 3));
    }
  }

  Public String getType()
  {
    return "Regular";
  }
  Public void returnMovie()
  {
    available = true;
  }
}
